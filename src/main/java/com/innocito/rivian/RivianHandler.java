package com.innocito.rivian;

import com.innocito.common.cache.MemoryCachingFactory;
import com.innocito.common.config.BrandConfig;
import com.innocito.common.exception.GraphQlException;
import com.innocito.common.exception.NotFoundException;
import com.innocito.common.exception.ServerUnavailableException;
import com.innocito.rivian.dto.RivianCreateCSRFTokenDTO;
import com.innocito.rivian.dto.RivianLoginDTO;
import com.innocito.rivian.dto.RivianSendCommandDTO;
import com.innocito.rivian.dto.RivianUserInfoDTO;
import com.innocito.rivian.dto.RivianVehicleStateDTO;
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
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.GraphQlClientException;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import static com.innocito.common.constants.AppConstants.DOT_DELIMITER;
import static com.innocito.common.constants.AppConstants.DOT_DELIMITER_REGEX;
import static com.innocito.common.exception.ErrorMessages.NOT_FOUND;

@Service
@Slf4j
public class RivianHandler implements BrandTypeHandler {
  @Autowired()
  private BrandConfig brandConfig;

  private BrandConfig.RivianConfig rivianConfig;

  @Autowired
  private MemoryCachingFactory memoryCachingFactory;

  @PostConstruct
  public void init() {
    try {
      rivianConfig = brandConfig.getRivian();
      getAccessToken();
    } catch (Exception exception) {
      log.error(exception.getMessage());
    }
  }

  @Override
  public BrandType getBrandType() {
    return BrandType.RIVIAN;
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
    RivianUserInfoDTO rivianUserInfoDTO = getUserInfo();
    RivianUserInfoDTO.UserVehicleDTO matchedVehicleVin = rivianUserInfoDTO.getVehicles().stream()
      .filter(userVehicleDTO -> vin.equalsIgnoreCase(userVehicleDTO.getVin()))
      .findAny()
      .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "VIN")));
    return VehicleIdDTO.builder()
      .vehicleId(matchedVehicleVin.getId())
      .vin(matchedVehicleVin.getVin())
      .build();
  }

  @Override
  public VehicleInfoDTO getVehicleInfoByID(String vehicleID) {
    VehicleInfoDTO vehicleInfoDTO =
      VehicleInfoDTOWrapper.buildVehicleInfoDTOFromRivian(getVehicleState(vehicleID));
    vehicleInfoDTO.setVehicleId(vehicleID);
    return vehicleInfoDTO;
  }

  @Override
  public SendCommandResponseDTO sendVehicleCommand(String vehicleID, CommandType commandType) {
    RivianSendCommandDTO rivianSendCommandDTO = sendCommandRequest(CommandMapper.mapToRivian(commandType), vehicleID);
    return SendCommandResponseDTO.builder()
      .command(commandType.getValue())
      .status(rivianSendCommandDTO.getState() == 0)
      .build();
  }

  private void getCSRFToken() {
    String query = "mutation CreateCSRFToken { createCsrfToken { __typename csrfToken appSessionToken } }";

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
    };
    RivianCreateCSRFTokenDTO rivianCreateCSRFTokenDTO = makeGQLCallToRivian(headersConsumer, query, "CreateCSRFToken",
      Collections.emptyMap(), "createCsrfToken", RivianCreateCSRFTokenDTO.class);
    memoryCachingFactory.addToCache("rivian-a-sess", rivianCreateCSRFTokenDTO.getCsrfToken());
    memoryCachingFactory.addToCache("rivian-csrf-token", rivianCreateCSRFTokenDTO.getAppSessionToken());
  }

  private void getAccessToken() {
    String accessToken;
    accessToken = memoryCachingFactory.getFromCache("rivian-accessToken", String.class);
    if (isValidAccessToken(accessToken)) {
      return;
    }

    getCSRFToken();

    String query = "mutation Login($email: String!, $password: String!) { login(email: $email, password: $password) { __typename ... on MobileLoginResponse { accessToken refreshToken userSessionToken } ... on MobileMFALoginResponse { otpToken } } }";
    Map<String, Object> variables = Map.of(
      "email", rivianConfig.getEmail(),
      "password", rivianConfig.getPassword()
    );

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("a-sess", memoryCachingFactory.getFromCache("rivian-a-sess", String.class));
      headers.set("csrf-token", memoryCachingFactory.getFromCache("rivian-csrf-token", String.class));
      headers.set("apollographql-client-name", "com.rivian.android.consumer");
    };
    RivianLoginDTO rivianLoginDTO = makeGQLCallToRivian(headersConsumer, query, "Login", variables, "loginWithOTP",
      RivianLoginDTO.class);
    memoryCachingFactory.addToCache("rivian-accessToken", rivianLoginDTO.getAccessToken());
    memoryCachingFactory.addToCache("rivian-refreshToken", rivianLoginDTO.getRefreshToken());
    memoryCachingFactory.addToCache("rivian-u-sess", rivianLoginDTO.getUserSessionToken());
  }

  private RivianUserInfoDTO getUserInfo() {
    String query = "query getUserInfo { currentUser { __typename id firstName lastName email address { __typename " +
      "country } vehicles { __typename id name owner roles vin vas { __typename vasVehicleId vehiclePublicKey } vehicle { __typename model mobileConfiguration { __typename trimOption { __typename optionId optionName } exteriorColorOption { __typename optionId optionName } interiorColorOption { __typename optionId optionName } } vehicleState { __typename supportedFeatures { __typename name status } } otaEarlyAccessStatus } settings { __typename name { __typename value } } } enrolledPhones { __typename vas { __typename vasPhoneId publicKey } enrolled { __typename deviceType deviceName vehicleId identityId shortName } } pendingInvites { __typename id invitedByFirstName role status vehicleId vehicleModel email } } }";

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("a-sess", memoryCachingFactory.getFromCache("rivian-a-sess", String.class));
      headers.set("csrf-token", memoryCachingFactory.getFromCache("rivian-csrf-token", String.class));
      headers.set("u-sess", memoryCachingFactory.getFromCache("rivian-u-sess", String.class));
    };
    return makeGQLCallToRivian(headersConsumer, query, "getUserInfo", Collections.emptyMap(), "currentUser",
      RivianUserInfoDTO.class);
  }

  private RivianVehicleStateDTO getVehicleState(String vehicleId) {
    String query = "query GetVehicleState($vehicleID: String!) { vehicleState(id: $vehicleID) { __typename " +
      "gnssLocation { __typename latitude longitude timeStamp } alarmSoundStatus { __typename timeStamp value } timeToEndOfCharge { __typename timeStamp value } doorFrontLeftLocked { __typename timeStamp value } doorFrontLeftClosed { __typename timeStamp value } doorFrontRightLocked { __typename timeStamp value } doorFrontRightClosed { __typename timeStamp value } doorRearLeftLocked { __typename timeStamp value } doorRearLeftClosed { __typename timeStamp value } doorRearRightLocked { __typename timeStamp value } doorRearRightClosed { __typename timeStamp value } windowFrontLeftClosed { __typename timeStamp value } windowFrontRightClosed { __typename timeStamp value } windowFrontLeftCalibrated { __typename timeStamp value } windowFrontRightCalibrated { __typename timeStamp value } windowRearLeftCalibrated { __typename timeStamp value } windowRearRightCalibrated { __typename timeStamp value } closureFrunkLocked { __typename timeStamp value } closureFrunkClosed { __typename timeStamp value } gearGuardLocked { __typename timeStamp value } closureLiftgateLocked { __typename timeStamp value } closureLiftgateClosed { __typename timeStamp value } windowRearLeftClosed { __typename timeStamp value } windowRearRightClosed { __typename timeStamp value } closureSideBinLeftLocked { __typename timeStamp value } closureSideBinLeftClosed { __typename timeStamp value } closureSideBinRightLocked { __typename timeStamp value } closureSideBinRightClosed { __typename timeStamp value } closureTailgateLocked { __typename timeStamp value } closureTailgateClosed { __typename timeStamp value } closureTonneauLocked { __typename timeStamp value } closureTonneauClosed { __typename timeStamp value } wiperFluidState { __typename timeStamp value } powerState { __typename timeStamp value } batteryHvThermalEventPropagation { __typename timeStamp value } vehicleMileage { __typename timeStamp value } brakeFluidLow { __typename timeStamp value } gearStatus { __typename timeStamp value } tirePressureStatusFrontLeft { __typename timeStamp value } tirePressureStatusValidFrontLeft { __typename timeStamp value } tirePressureStatusFrontRight { __typename timeStamp value } tirePressureStatusValidFrontRight { __typename timeStamp value } tirePressureStatusRearLeft { __typename timeStamp value } tirePressureStatusValidRearLeft { __typename timeStamp value } tirePressureStatusRearRight { __typename timeStamp value } tirePressureStatusValidRearRight { __typename timeStamp value } batteryLevel { __typename timeStamp value } chargerState { __typename timeStamp value } batteryLimit { __typename timeStamp value } remoteChargingAvailable { __typename timeStamp value } batteryHvThermalEvent { __typename timeStamp value } rangeThreshold { __typename timeStamp value } distanceToEmpty { __typename timeStamp value } otaAvailableVersionNumber { __typename timeStamp value } otaAvailableVersionWeek { __typename timeStamp value } otaAvailableVersionYear { __typename timeStamp value } otaCurrentVersionNumber { __typename timeStamp value } otaCurrentVersionWeek { __typename timeStamp value } otaCurrentVersionYear { __typename timeStamp value } otaDownloadProgress { __typename timeStamp value } otaInstallDuration { __typename timeStamp value } otaInstallProgress { __typename timeStamp value } otaInstallReady { __typename timeStamp value } otaInstallTime { __typename timeStamp value } otaInstallType { __typename timeStamp value } otaStatus { __typename timeStamp value } otaCurrentStatus { __typename timeStamp value } cabinClimateInteriorTemperature { __typename timeStamp value } cabinPreconditioningStatus { __typename timeStamp value } cabinPreconditioningType { __typename timeStamp value } petModeStatus { __typename timeStamp value } petModeTemperatureStatus { __typename timeStamp value } cabinClimateDriverTemperature { __typename timeStamp value } gearGuardVideoStatus { __typename timeStamp value } gearGuardVideoMode { __typename timeStamp value } gearGuardVideoTermsAccepted { __typename timeStamp value } defrostDefogStatus { __typename timeStamp value } steeringWheelHeat { __typename timeStamp value } seatFrontLeftHeat { __typename timeStamp value } seatFrontRightHeat { __typename timeStamp value } seatRearLeftHeat { __typename timeStamp value } seatRearRightHeat { __typename timeStamp value } chargerStatus { __typename timeStamp value } seatFrontLeftVent { __typename timeStamp value } seatFrontRightVent { __typename timeStamp value } chargerDerateStatus { __typename timeStamp value } driveMode { __typename timeStamp value } } }";

    Map<String, Object> variables = Map.of(
      "vehicleID", vehicleId
    );

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("a-sess", memoryCachingFactory.getFromCache("rivian-a-sess", String.class));
      headers.set("csrf-token", memoryCachingFactory.getFromCache("rivian-csrf-token", String.class));
      headers.set("u-sess", memoryCachingFactory.getFromCache("rivian-u-sess", String.class));
    };
    return makeGQLCallToRivian(headersConsumer, query, "GetVehicleState", variables, "vehicleState",
      RivianVehicleStateDTO.class);
  }

  private RivianSendCommandDTO sendCommandRequest(RivianCommand rivianCommand, String vehicleId) {

    long timestamp = Instant.now().getEpochSecond();
    String message = rivianCommand.toValue() + timestamp;
    String hmac = HmacUtils.hmacSha256Hex(rivianConfig.getSecretKey(), message);

    String query = "mutation sendVehicleCommand($attrs: VehicleCommandAttributes!) { sendVehicleCommand(attrs: " +
      "$attrs) { __typename id command state } }";

    Map<String, Object> attrs = Map.of(
      "command", rivianCommand.toValue(),
      "hmac", hmac,
      "timestamp", timestamp,
      "vasPhoneId", rivianConfig.getVasPhoneId(),
      "deviceId", rivianConfig.getDeviceId(),
      "vehicleId", vehicleId
    );
    Map<String, Object> variables = Map.of(
      "attrs", attrs
    );

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("a-sess", memoryCachingFactory.getFromCache("rivian-a-sess", String.class));
      headers.set("csrf-token", memoryCachingFactory.getFromCache("rivian-csrf-token", String.class));
      headers.set("u-sess", memoryCachingFactory.getFromCache("rivian-u-sess", String.class));
    };

    return makeGQLCallToRivian(headersConsumer, query, "sendVehicleCommand", variables, "sendVehicleCommand",
      RivianSendCommandDTO.class);
  }

  private <T> T makeGQLCallToRivian(Consumer<HttpHeaders> headers, String query, String operationName,
                                    Map<String, Object> variables,
                                    String retrieve, Class<T> responseType) {
    GraphQlClient graphQlClient = HttpGraphQlClient.builder().url(rivianConfig.getUrl())
      .headers(headers)
      .build();
    try {
      T response = graphQlClient.document(query)
        .operationName(operationName)
        .variables(variables)
        .retrieve(retrieve)
        .toEntity(responseType)
        .block();

      return response;
    } catch (GraphQlClientException graphQlClientException) {
      if (graphQlClientException.getCause() instanceof WebClientResponseException webClientResponseException) {
        String errorBody = webClientResponseException.getResponseBodyAsString();
        HttpStatusCode statusCode = webClientResponseException.getStatusCode();
        throw new GraphQlException("HTTP " + statusCode.value() + " error: " + errorBody, graphQlClientException);
      }
      throw new GraphQlException("GraphQL error: " + graphQlClientException.getMessage(), graphQlClientException);
    } catch (Exception exception) {
      throw new ServerUnavailableException(exception.getMessage());
    }
  }
}