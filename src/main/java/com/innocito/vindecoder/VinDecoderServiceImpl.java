package com.innocito.vindecoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.innocito.common.exception.ErrorMessages;
import com.innocito.common.exception.ExternalApiException;
import com.innocito.common.exception.JsonException;
import com.innocito.common.exception.ServerUnavailableException;
import com.innocito.vindecoder.dto.VinDecoderDTO;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.innocito.common.constants.AppConstants.RESULTS;
import static com.innocito.common.exception.ErrorMessages.UNEXPECTED_ERROR;

@Slf4j
@Service
public class VinDecoderServiceImpl implements VinDecoderService {
  @Autowired
  @Qualifier("restTemplateWithConnectReadTimeout")
  RestTemplate restTemplate;

  @Override
  public VinDecoderDTO getVehicleDetailsByVin(String vin) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Object> requestEntity = new HttpEntity<>(StringUtils.EMPTY, httpHeaders);
    try {
      String url = UriComponentsBuilder.fromUriString("https://vpic.nhtsa.dot.gov/api/vehicles/DecodeVinValuesExtended/{vin}")
        .queryParam("format", "json")
        .buildAndExpand(vin)
        .toUriString();
      ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
        new ParameterizedTypeReference<>() {
        }, vin);
      VinDecoderDTO vinDecoderDTO = VinDecoderDTO.builder()
        .build();
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
        JsonNode dataNode = jsonNode.path(RESULTS);
        if (dataNode.isArray() && !dataNode.isEmpty()) {
          vinDecoderDTO = mapper.treeToValue(dataNode.get(0), VinDecoderDTO.class);
        }
      }
      return vinDecoderDTO;
    } catch (JsonProcessingException exception) {
      throw new JsonException(ErrorMessages.JSON_PARSE_ERROR + exception.getMessage(), exception);
    } catch (ServerUnavailableException exception) {
      throw new ServerUnavailableException(ErrorMessages.SERVER_DOWN);
    } catch (Exception exception) {
      throw new ExternalApiException(String.format(UNEXPECTED_ERROR, exception.getMessage()), exception);
    }
  }
}
