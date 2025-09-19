package com.innocito.rivian;

import com.innocito.common.config.RivianConfig;
import com.innocito.common.exception.GraphQlException;
import com.innocito.common.exception.NotFoundException;
import com.innocito.common.exception.ServerUnavailableException;
import com.innocito.rivian.dto.CreateCSRFTokenDTO;
import com.innocito.rivian.dto.LoginDTO;
import com.innocito.rivian.dto.UserInfoDTO;
import com.innocito.rivian.dto.VehicleStateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.GraphQlClientException;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import static com.innocito.common.exception.ErrorMessages.NOT_FOUND;

@Service
@Slf4j
public class RivianVehicleServiceImpl implements RivianVehicleService {
  @Autowired
  private RivianConfig rivianConfig;

  @Override
  public VehicleStateDTO getVehicleInfoByVin(String vin) {
    CreateCSRFTokenDTO createCSRFTokenDTO = getCSRFToken();
    LoginDTO loginDTO = getAccessToken(createCSRFTokenDTO);
    UserInfoDTO userInfoDTO = getUserInfo(createCSRFTokenDTO, loginDTO);
    UserInfoDTO.UserVehicleDTO matchedVehicleVin = userInfoDTO.getVehicles().stream()
      .filter(userVehicleDTO -> vin.equalsIgnoreCase(userVehicleDTO.getVin()))
      .findAny()
      .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "VIN")));
    return getVehicleState(createCSRFTokenDTO, loginDTO, matchedVehicleVin.getId());
  }

  private CreateCSRFTokenDTO getCSRFToken() {
    String query = "mutation CreateCSRFToken { createCsrfToken { __typename csrfToken appSessionToken } }";

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
    };
    return makeGQLCallToRivian(headersConsumer, query, "CreateCSRFToken", Collections.emptyMap(), "createCsrfToken",
      CreateCSRFTokenDTO.class);
  }

  private LoginDTO getAccessToken(CreateCSRFTokenDTO createCSRFTokenDTO) {
    String query = "mutation Login($email: String!, $password: String!) { login(email: $email, password: $password) { __typename ... on MobileLoginResponse { accessToken refreshToken userSessionToken } ... on MobileMFALoginResponse { otpToken } } }";
    Map<String, Object> variables = Map.of(
      "email", rivianConfig.getEmail(),
      "password", rivianConfig.getPassword()
    );

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("a-sess", createCSRFTokenDTO.getAppSessionToken());
      headers.set("csrf-token", createCSRFTokenDTO.getCsrfToken());
      headers.set("apollographql-client-name", "com.rivian.android.consumer");
    };
    return makeGQLCallToRivian(headersConsumer, query, "Login", variables, "loginWithOTP",
      LoginDTO.class);

  }

  private UserInfoDTO getUserInfo(CreateCSRFTokenDTO createCSRFTokenDTO, LoginDTO loginDTO) {
    String query = "query getUserInfo { currentUser { __typename id firstName lastName email address { __typename " +
      "country } vehicles { __typename id name owner roles vin vas { __typename vasVehicleId vehiclePublicKey } vehicle { __typename model mobileConfiguration { __typename trimOption { __typename optionId optionName } exteriorColorOption { __typename optionId optionName } interiorColorOption { __typename optionId optionName } } vehicleState { __typename supportedFeatures { __typename name status } } otaEarlyAccessStatus } settings { __typename name { __typename value } } } enrolledPhones { __typename vas { __typename vasPhoneId publicKey } enrolled { __typename deviceType deviceName vehicleId identityId shortName } } pendingInvites { __typename id invitedByFirstName role status vehicleId vehicleModel email } } }";

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("a-sess", createCSRFTokenDTO.getAppSessionToken());
      headers.set("csrf-token", createCSRFTokenDTO.getCsrfToken());
      headers.set("u-sess", loginDTO.getUserSessionToken());
    };
    return makeGQLCallToRivian(headersConsumer, query, "getUserInfo", Collections.emptyMap(), "currentUser",
      UserInfoDTO.class);
  }

  private VehicleStateDTO getVehicleState(CreateCSRFTokenDTO createCSRFTokenDTO, LoginDTO loginDTO,
                                          String vehicleId) {
    String query = "query GetVehicleState($vehicleID: String!) { vehicleState(id: $vehicleID) { __typename " +
      "gnssLocation { __typename latitude longitude timeStamp } alarmSoundStatus { __typename timeStamp value } timeToEndOfCharge { __typename timeStamp value } doorFrontLeftLocked { __typename timeStamp value } doorFrontLeftClosed { __typename timeStamp value } doorFrontRightLocked { __typename timeStamp value } doorFrontRightClosed { __typename timeStamp value } doorRearLeftLocked { __typename timeStamp value } doorRearLeftClosed { __typename timeStamp value } doorRearRightLocked { __typename timeStamp value } doorRearRightClosed { __typename timeStamp value } windowFrontLeftClosed { __typename timeStamp value } windowFrontRightClosed { __typename timeStamp value } windowFrontLeftCalibrated { __typename timeStamp value } windowFrontRightCalibrated { __typename timeStamp value } windowRearLeftCalibrated { __typename timeStamp value } windowRearRightCalibrated { __typename timeStamp value } closureFrunkLocked { __typename timeStamp value } closureFrunkClosed { __typename timeStamp value } gearGuardLocked { __typename timeStamp value } closureLiftgateLocked { __typename timeStamp value } closureLiftgateClosed { __typename timeStamp value } windowRearLeftClosed { __typename timeStamp value } windowRearRightClosed { __typename timeStamp value } closureSideBinLeftLocked { __typename timeStamp value } closureSideBinLeftClosed { __typename timeStamp value } closureSideBinRightLocked { __typename timeStamp value } closureSideBinRightClosed { __typename timeStamp value } closureTailgateLocked { __typename timeStamp value } closureTailgateClosed { __typename timeStamp value } closureTonneauLocked { __typename timeStamp value } closureTonneauClosed { __typename timeStamp value } wiperFluidState { __typename timeStamp value } powerState { __typename timeStamp value } batteryHvThermalEventPropagation { __typename timeStamp value } vehicleMileage { __typename timeStamp value } brakeFluidLow { __typename timeStamp value } gearStatus { __typename timeStamp value } tirePressureStatusFrontLeft { __typename timeStamp value } tirePressureStatusValidFrontLeft { __typename timeStamp value } tirePressureStatusFrontRight { __typename timeStamp value } tirePressureStatusValidFrontRight { __typename timeStamp value } tirePressureStatusRearLeft { __typename timeStamp value } tirePressureStatusValidRearLeft { __typename timeStamp value } tirePressureStatusRearRight { __typename timeStamp value } tirePressureStatusValidRearRight { __typename timeStamp value } batteryLevel { __typename timeStamp value } chargerState { __typename timeStamp value } batteryLimit { __typename timeStamp value } remoteChargingAvailable { __typename timeStamp value } batteryHvThermalEvent { __typename timeStamp value } rangeThreshold { __typename timeStamp value } distanceToEmpty { __typename timeStamp value } otaAvailableVersionNumber { __typename timeStamp value } otaAvailableVersionWeek { __typename timeStamp value } otaAvailableVersionYear { __typename timeStamp value } otaCurrentVersionNumber { __typename timeStamp value } otaCurrentVersionWeek { __typename timeStamp value } otaCurrentVersionYear { __typename timeStamp value } otaDownloadProgress { __typename timeStamp value } otaInstallDuration { __typename timeStamp value } otaInstallProgress { __typename timeStamp value } otaInstallReady { __typename timeStamp value } otaInstallTime { __typename timeStamp value } otaInstallType { __typename timeStamp value } otaStatus { __typename timeStamp value } otaCurrentStatus { __typename timeStamp value } cabinClimateInteriorTemperature { __typename timeStamp value } cabinPreconditioningStatus { __typename timeStamp value } cabinPreconditioningType { __typename timeStamp value } petModeStatus { __typename timeStamp value } petModeTemperatureStatus { __typename timeStamp value } cabinClimateDriverTemperature { __typename timeStamp value } gearGuardVideoStatus { __typename timeStamp value } gearGuardVideoMode { __typename timeStamp value } gearGuardVideoTermsAccepted { __typename timeStamp value } defrostDefogStatus { __typename timeStamp value } steeringWheelHeat { __typename timeStamp value } seatFrontLeftHeat { __typename timeStamp value } seatFrontRightHeat { __typename timeStamp value } seatRearLeftHeat { __typename timeStamp value } seatRearRightHeat { __typename timeStamp value } chargerStatus { __typename timeStamp value } seatFrontLeftVent { __typename timeStamp value } seatFrontRightVent { __typename timeStamp value } chargerDerateStatus { __typename timeStamp value } driveMode { __typename timeStamp value } } }";

    Map<String, Object> variables = Map.of(
      "vehicleID", vehicleId
    );

    Consumer<HttpHeaders> headersConsumer = headers -> {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("a-sess", createCSRFTokenDTO.getAppSessionToken());
      headers.set("csrf-token", createCSRFTokenDTO.getCsrfToken());
      headers.set("u-sess", loginDTO.getUserSessionToken());
    };
    return makeGQLCallToRivian(headersConsumer, query, "GetVehicleState", variables, "vehicleState",
      VehicleStateDTO.class);
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
        int statusCode = webClientResponseException.getRawStatusCode();
        throw new GraphQlException("HTTP " + statusCode + " error: " + errorBody, graphQlClientException);
      }
      throw new GraphQlException("GraphQL error: " + graphQlClientException.getMessage(), graphQlClientException);
    } catch (Exception exception) {
      throw new ServerUnavailableException(exception.getMessage());
    }
  }
}
