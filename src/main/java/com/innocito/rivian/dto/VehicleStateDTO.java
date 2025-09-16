package com.innocito.rivian.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleStateDTO {
  private GNSSLocation gnssLocation;
  private VehicleStateParam<Boolean> alarmSoundStatus;
  private VehicleStateParam<Integer> timeToEndOfCharge;

  private VehicleStateParam<String> doorFrontLeftLocked;
  private VehicleStateParam<String> doorFrontLeftClosed;
  private VehicleStateParam<String> doorFrontRightLocked;
  private VehicleStateParam<String> doorFrontRightClosed;
  private VehicleStateParam<String> doorRearLeftLocked;
  private VehicleStateParam<String> doorRearLeftClosed;
  private VehicleStateParam<String> doorRearRightLocked;
  private VehicleStateParam<String> doorRearRightClosed;

  private VehicleStateParam<String> windowFrontLeftClosed;
  private VehicleStateParam<String> windowFrontRightClosed;
  private VehicleStateParam<String> windowRearLeftClosed;
  private VehicleStateParam<String> windowRearRightClosed;

  private VehicleStateParam<String> windowFrontLeftCalibrated;
  private VehicleStateParam<String> windowFrontRightCalibrated;
  private VehicleStateParam<String> windowRearLeftCalibrated;
  private VehicleStateParam<String> windowRearRightCalibrated;

  private VehicleStateParam<String> closureFrunkLocked;
  private VehicleStateParam<String> closureFrunkClosed;
  private VehicleStateParam<String> closureFrunkNextAction;

  private VehicleStateParam<String> gearGuardLocked;

  private VehicleStateParam<String> closureLiftgateLocked;
  private VehicleStateParam<String> closureLiftgateClosed;
  private VehicleStateParam<String> closureLiftgateNextAction;

  private VehicleStateParam<String> closureSideBinLeftLocked;
  private VehicleStateParam<String> closureSideBinLeftClosed;
  private VehicleStateParam<String> closureSideBinLeftNextAction;
  private VehicleStateParam<String> closureSideBinRightLocked;
  private VehicleStateParam<String> closureSideBinRightClosed;
  private VehicleStateParam<String> closureSideBinRightNextAction;

  private VehicleStateParam<String> closureTailgateLocked;
  private VehicleStateParam<String> closureTailgateClosed;
  private VehicleStateParam<String> closureTailgateNextAction;

  private VehicleStateParam<String> closureTonneauLocked;
  private VehicleStateParam<String> closureTonneauClosed;

  private VehicleStateParam<String> wiperFluidState;
  private VehicleStateParam<String> powerState;

  private VehicleStateParam<String> batteryHvThermalEventPropagation;

  private VehicleStateParam<Long> vehicleMileage;

  private VehicleStateParam<String> brakeFluidLow;

  private VehicleStateParam<String> gearStatus;

  private VehicleStateParam<String> tirePressureStatusFrontLeft;
  private VehicleStateParam<String> tirePressureStatusValidFrontLeft;
  private VehicleStateParam<String> tirePressureStatusFrontRight;
  private VehicleStateParam<String> tirePressureStatusValidFrontRight;
  private VehicleStateParam<String> tirePressureStatusRearLeft;
  private VehicleStateParam<String> tirePressureStatusValidRearLeft;
  private VehicleStateParam<String> tirePressureStatusRearRight;
  private VehicleStateParam<String> tirePressureStatusValidRearRight;

  private VehicleStateParam<Double> batteryLevel;
  private VehicleStateParam<String> chargerState;
  private VehicleStateParam<Integer> batteryLimit;
  private VehicleStateParam<Integer> remoteChargingAvailable;
  private VehicleStateParam<String> batteryHvThermalEvent;

  private VehicleStateParam<String> rangeThreshold;
  private VehicleStateParam<Integer> distanceToEmpty;

  private VehicleStateParam<String> otaAvailableVersionGitHash;
  private VehicleStateParam<Integer> otaAvailableVersionNumber;
  private VehicleStateParam<Integer> otaAvailableVersionWeek;
  private VehicleStateParam<Integer> otaAvailableVersionYear;

  private VehicleStateParam<String> otaCurrentVersionGitHash;
  private VehicleStateParam<Integer> otaCurrentVersionNumber;
  private VehicleStateParam<Integer> otaCurrentVersionWeek;
  private VehicleStateParam<Integer> otaCurrentVersionYear;

  private VehicleStateParam<Integer> otaDownloadProgress;
  private VehicleStateParam<Integer> otaInstallDuration;
  private VehicleStateParam<Integer> otaInstallProgress;
  private VehicleStateParam<String> otaInstallReady;
  private VehicleStateParam<Integer> otaInstallTime;
  private VehicleStateParam<String> otaInstallType;
  private VehicleStateParam<String> otaStatus;
  private VehicleStateParam<String> otaCurrentStatus;

  private VehicleStateParam<Integer> cabinClimateInteriorTemperature;
  private VehicleStateParam<String> cabinPreconditioningStatus;
  private VehicleStateParam<String> cabinPreconditioningType;
  private VehicleStateParam<String> petModeStatus;
  private VehicleStateParam<String> petModeTemperatureStatus;
  private VehicleStateParam<Integer> cabinClimateDriverTemperature;

  private VehicleStateParam<String> gearGuardVideoStatus;
  private VehicleStateParam<String> gearGuardVideoMode;
  private VehicleStateParam<String> gearGuardVideoTermsAccepted;

  private VehicleStateParam<String> defrostDefogStatus;
  private VehicleStateParam<String> steeringWheelHeat;
  private VehicleStateParam<String> seatFrontLeftHeat;
  private VehicleStateParam<String> seatFrontRightHeat;
  private VehicleStateParam<String> seatRearLeftHeat;
  private VehicleStateParam<String> seatRearRightHeat;

  private VehicleStateParam<String> chargerStatus;

  private VehicleStateParam<String> seatFrontLeftVent;
  private VehicleStateParam<String> seatFrontRightVent;

  private VehicleStateParam<String> chargerDerateStatus;

  private VehicleStateParam<String> driveMode;
  private VehicleStateParam<String> serviceMode;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class GNSSLocation {
    private String latitude;
    private String longitude;
    private String timeStamp;
    private boolean isAuthorized;
  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class VehicleStateParam<T> {
    private String timeStamp;
    private T value;
  }
}