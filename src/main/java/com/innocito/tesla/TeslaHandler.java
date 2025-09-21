package com.innocito.tesla;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocito.common.cache.MemoryCachingFactory;
import com.innocito.common.config.BrandConfig;
import com.innocito.common.exception.ErrorMessages;
import com.innocito.common.exception.ExternalApiException;
import com.innocito.common.exception.JsonException;
import com.innocito.tesla.dto.TeslaAccessTokenDTO;
import com.innocito.tesla.dto.TeslaSendVehicleCommadDTO;
import com.innocito.tesla.dto.TeslaVehicleDataDTO;
import com.innocito.vehicle.BrandType;
import com.innocito.vehicle.BrandTypeHandler;
import com.innocito.vehicle.CommandType;
import com.innocito.vehicle.dto.CommandMapper;
import com.innocito.vehicle.dto.SendCommandResponseDTO;
import com.innocito.vehicle.dto.VehicleIdDTO;
import com.innocito.vehicle.dto.VehicleInfoDTO;
import com.innocito.vehicle.dto.VehicleInfoDTOWrapper;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static com.innocito.common.constants.AppConstants.DOT_DELIMITER;
import static com.innocito.common.constants.AppConstants.DOT_DELIMITER_REGEX;
import static com.innocito.common.exception.ErrorMessages.UNEXPECTED_ERROR;

@Service
@Slf4j
public class TeslaHandler implements BrandTypeHandler {
  @Autowired
  @Qualifier("restTemplateWithConnectReadTimeout")
  RestTemplate restTemplate;
  @Autowired
  MemoryCachingFactory memoryCachingFactory;
  @Autowired()
  private BrandConfig brandConfig;
  private BrandConfig.TeslaConfig teslaConfig;

  @PostConstruct
  public void init() {
    try {
      teslaConfig = brandConfig.getTesla();
      getAccessToken();
    } catch (Exception exception) {
      log.error(exception.getMessage());
    }
  }

  @Override
  public BrandType getBrandType() {
    return BrandType.TESLA;
  }

  private boolean isValidAccessToken(String accessToken) {
    if (StringUtils.isNotEmpty(accessToken)) {
      String[] splitToken = accessToken.split(DOT_DELIMITER_REGEX);
      String unsignedToken = splitToken[0] + DOT_DELIMITER + splitToken[1] + DOT_DELIMITER;
      try {
        Jwts.parserBuilder().build().parse(unsignedToken);
      } catch (Exception exception) {
        return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public VehicleIdDTO getVehicleByVin(String vin) {
    return getVehicle(vin);
  }

  @Override
  public VehicleInfoDTO getVehicleInfoByID(String vehicleID) {
    return VehicleInfoDTOWrapper.buildVehicleInfoDTOFromTesla(getVehicleData(vehicleID));
  }

  @Override
  public SendCommandResponseDTO sendVehicleCommand(String vehicleID, CommandType commandType) {
    TeslaVehicleCommand teslaVehicleCommand = CommandMapper.mapToTesla(commandType);
    Map<String, Object> requestBody = new HashMap<>();
    switch (teslaVehicleCommand) {
      case DOOR_LOCK:
        break;
      case DOOR_UNLOCK:
        break;
      case ACTUATE_TRUNK:
        requestBody.put("which_trunk", "front");
        break;
      default:
        throw new IllegalArgumentException("Unsupported Tesla command: " + teslaVehicleCommand);

    }
    TeslaSendVehicleCommadDTO teslaSendVehicleCommadDTO = sendCommandRequest(teslaVehicleCommand.toValue(), requestBody
      , vehicleID);

    return SendCommandResponseDTO.builder()
      .command(commandType.getValue())
      .status(teslaSendVehicleCommadDTO.isResult())
      .build();
  }

  private void getAccessToken() {
    String accessToken;
    accessToken = memoryCachingFactory.getFromCache("tesla-accessToken", String.class);
    if (isValidAccessToken(accessToken)) {
      return;
    }
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

    requestBody.add("client_id", teslaConfig.getClientId());
    requestBody.add("client_secret", teslaConfig.getClientSecret());
    requestBody.add("grant_type", teslaConfig.getGrantType());
    requestBody.add("scope", teslaConfig.getScope());
    requestBody.add("audience", teslaConfig.getAudience());

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
    try {
      String url = UriComponentsBuilder.fromUriString(teslaConfig.getUrl())
        .path("/oauth2/v3/token")
        .build()
        .toUriString();

      ResponseEntity<TeslaAccessTokenDTO> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
        new ParameterizedTypeReference<>() {
        });
      if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
        TeslaAccessTokenDTO teslaAccessTokenDTO = responseEntity.getBody();
        memoryCachingFactory.addToCache("tesla-accessToken", teslaAccessTokenDTO.getAccessToken());
        // TODO: we can add expiry time if we know it in sec/ms
        memoryCachingFactory.addToCache("tesla-refreshToken", teslaAccessTokenDTO.getRefreshToken());
      } else {
        throw new ExternalApiException("Failed to get access token. Status: " + responseEntity.getStatusCode());
      }

    } catch (HttpClientErrorException | HttpServerErrorException exception) {
      throw new ExternalApiException("Error response from Tesla Auth API: " + exception.getResponseBodyAsString(), exception);
    } catch (Exception exception) {
      throw new ExternalApiException(String.format(UNEXPECTED_ERROR, exception.getMessage()), exception);
    }
  }

  private VehicleIdDTO getVehicle(String vin) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setBearerAuth(memoryCachingFactory.getFromCache("tesla-accessToken", String.class));

    HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
    try {
      String url = UriComponentsBuilder.fromUriString(teslaConfig.getUrl())
        .path("/api/1/vehicles/{vehicle_tag}")
        .buildAndExpand(vin)
        .toUriString();

      ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
        new ParameterizedTypeReference<>() {
        });
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
        JsonNode responseNode = jsonNode.path("response");

        if (!responseNode.isMissingNode()) {
          String responseVin = responseNode.path("vin").asText(null);
          String vehicleId = responseNode.path("vehicle_id").asText(null);

          return VehicleIdDTO.builder()
            .vin(responseVin)
            .vehicleId(vehicleId)
            .build();
        } else {
          throw new ExternalApiException("Missing 'response' node in Tesla API response");
        }
      } else {
        throw new ExternalApiException("Failed to fetch vehicle by VIN. Status: " + responseEntity.getStatusCode());
      }
    } catch (JsonProcessingException e) {
      throw new JsonException(ErrorMessages.JSON_PARSE_ERROR + e.getMessage(), e);
    } catch (HttpClientErrorException | HttpServerErrorException exception) {
      throw new ExternalApiException("Error response from Tesla Vehicle API: " + exception.getResponseBodyAsString(),
        exception);
    } catch (Exception e) {
      throw new ExternalApiException(String.format(UNEXPECTED_ERROR, e.getMessage()), e);
    }
  }


  private TeslaVehicleDataDTO getVehicleData(String vehicleId) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setBearerAuth(memoryCachingFactory.getFromCache("tesla-accessToken", String.class));

    HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
    try {
      String url = UriComponentsBuilder.fromUriString(teslaConfig.getUrl())
        .path("/api/1/vehicles/{vehicle_tag}/vehicle_data")
        .buildAndExpand(vehicleId)
        .toUriString();

      ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
        new ParameterizedTypeReference<>() {
        });
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
        JsonNode responseNode = jsonNode.path("response");
        return mapper.readValue(
          responseNode.traverse(), new TypeReference<TeslaVehicleDataDTO>() {
          });
      } else {
        throw new ExternalApiException("Failed to fetch vehicle data by vehicle ID. Status: " + responseEntity.getStatusCode());
      }
    } catch (JsonProcessingException e) {
      throw new JsonException(ErrorMessages.JSON_PARSE_ERROR + e.getMessage(), e);
    } catch (HttpClientErrorException | HttpServerErrorException exception) {
      throw new ExternalApiException("Error response from Tesla Vehicle data API: " + exception.getResponseBodyAsString(),
        exception);
    } catch (Exception e) {
      throw new ExternalApiException(String.format(UNEXPECTED_ERROR, e.getMessage()), e);
    }
  }

  private TeslaSendVehicleCommadDTO sendCommandRequest(String command, Map<String, Object> requestBody, String vehicleId) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setBearerAuth(memoryCachingFactory.getFromCache("tesla-accessToken", String.class));

    HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
    try {
      String url = UriComponentsBuilder.fromUriString(teslaConfig.getUrl())
        .path("/api/1/vehicles/{vehicle_tag}/command/{command}")
        .buildAndExpand(vehicleId, command.toLowerCase())
        .toUriString();

      ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
        new ParameterizedTypeReference<>() {
        });
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
        JsonNode responseNode = jsonNode.path("response");
        return mapper.readValue(
          responseNode.traverse(), new TypeReference<TeslaSendVehicleCommadDTO>() {
          });
      } else {
        throw new ExternalApiException("Failed to send vehicle command for vehicle ID for command: " + command + ". Status: " + responseEntity.getStatusCode());
      }
    } catch (JsonProcessingException e) {
      throw new JsonException(ErrorMessages.JSON_PARSE_ERROR + e.getMessage(), e);
    } catch (HttpClientErrorException | HttpServerErrorException exception) {
      throw new ExternalApiException("Error response from Tesla Vehicle send vehicle command API: " + exception.getResponseBodyAsString(),
        exception);
    } catch (Exception e) {
      throw new ExternalApiException(String.format(UNEXPECTED_ERROR, e.getMessage()), e);
    }
  }
}
