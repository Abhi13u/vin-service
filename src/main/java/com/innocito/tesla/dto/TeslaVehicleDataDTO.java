package com.innocito.tesla.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TeslaVehicleDataDTO {
  private Long id;
  private Long userId;
  private String vehicleId;
  private String vin;
  private String color;
  private String accessType;
  private GranularAccess granularAccess;
  private List<String> tokens;
  private String state;
  private Boolean inService;
  private String idS;
  private Boolean calendarEnabled;
  private Integer apiVersion;
  private String backseatToken;
  private Long backseatTokenUpdatedAt;

  private ChargeState chargeState;
  private ClimateState climateState;
  private DriveState driveState;
  private GuiSettings guiSettings;
  private VehicleConfig vehicleConfig;
  private VehicleState vehicleState;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class ChargeState {
    private Boolean batteryHeaterOn;
    private Integer batteryLevel;
    private Double batteryRange;
    private Integer chargeAmps;
    private Integer chargeCurrentRequest;
    private Integer chargeCurrentRequestMax;
    private Boolean chargeEnableRequest;
    private Double chargeEnergyAdded;
    private Integer chargeLimitSoc;
    private Integer chargeLimitSocMax;
    private Integer chargeLimitSocMin;
    private Integer chargeLimitSocStd;
    private Integer chargeMilesAddedIdeal;
    private Integer chargeMilesAddedRated;
    private Boolean chargePortColdWeatherMode;
    private String chargePortColor;
    private Boolean chargePortDoorOpen;
    private String chargePortLatch;
    private Integer chargeRate;
    private Integer chargerActualCurrent;
    private Integer chargerPhases;
    private Integer chargerPilotCurrent;
    private Integer chargerPower;
    private Integer chargerVoltage;
    private String chargingState;
    private String connChargeCable;
    private Double estBatteryRange;
    private String fastChargerBrand;
    private Boolean fastChargerPresent;
    private String fastChargerType;
    private Double idealBatteryRange;
    private Boolean managedChargingActive;
    private Long managedChargingStartTime;
    private Boolean managedChargingUserCanceled;
    private Integer maxRangeChargeCounter;
    private Integer minutesToFullCharge;
    private Boolean notEnoughPowerToHeat;
    private Boolean offPeakChargingEnabled;
    private String offPeakChargingTimes;
    private Integer offPeakHoursEndTime;
    private Boolean preconditioningEnabled;
    private String preconditioningTimes;
    private String scheduledChargingMode;
    private Boolean scheduledChargingPending;
    private Long scheduledChargingStartTime;
    private Long scheduledDepartureTime;
    private Integer scheduledDepartureTimeMinutes;
    private Boolean superchargerSessionTripPlanner;
    private Double timeToFullCharge;
    private Long timestamp;
    private Boolean tripCharging;
    private Integer usableBatteryLevel;
    private Boolean userChargeEnableRequest;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class ClimateState {
    private Boolean allowCabinOverheatProtection;
    private Boolean autoSeatClimateLeft;
    private Boolean autoSeatClimateRight;
    private Boolean autoSteeringWheelHeat;
    private Boolean batteryHeater;
    private Boolean batteryHeaterNoPower;
    private Boolean bioweaponMode;
    private String cabinOverheatProtection;
    private Boolean cabinOverheatProtectionActivelyCooling;
    private String climateKeeperMode;
    private String copActivationTemperature;
    private Integer defrostMode;
    private Double driverTempSetting;
    private Integer fanStatus;
    private String hvacAutoRequest;
    private Double insideTemp;
    private Boolean isAutoConditioningOn;
    private Boolean isClimateOn;
    private Boolean isFrontDefrosterOn;
    private Boolean isPreconditioning;
    private Boolean isRearDefrosterOn;
    private Integer leftTempDirection;
    private Double maxAvailTemp;
    private Double minAvailTemp;
    private Double outsideTemp;
    private Double passengerTempSetting;
    private Boolean remoteHeaterControlEnabled;
    private Integer rightTempDirection;
    private Integer seatHeaterLeft;
    private Integer seatHeaterRearCenter;
    private Integer seatHeaterRearLeft;
    private Integer seatHeaterRearRight;
    private Integer seatHeaterRight;
    private Boolean sideMirrorHeaters;
    private Integer steeringWheelHeatLevel;
    private Boolean steeringWheelHeater;
    private Boolean supportsFanOnlyCabinOverheatProtection;
    private Long timestamp;
    private Boolean wiperBladeHeater;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class DriveState {
    private Double activeRouteLatitude;
    private Double activeRouteLongitude;
    private Integer activeRouteTrafficMinutesDelay;
    private Long gpsAsOf;
    private Integer heading;
    private Double latitude;
    private Double longitude;
    private Double nativeLatitude;
    private Integer nativeLocationSupported;
    private Double nativeLongitude;
    private String nativeType;
    private Integer power;
    private String shiftState;
    private Integer speed;
    private Long timestamp;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class GuiSettings {
    private Boolean gui24HourTime;
    private String guiChargeRateUnits;
    private String guiDistanceUnits;
    private String guiRangeDisplay;
    private String guiTemperatureUnits;
    private String guiTirepressureUnits;
    private Boolean showRangeUnits;
    private Long timestamp;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class VehicleConfig {
    private String auxParkLamps;
    private Integer badgeVersion;
    private Boolean canAcceptNavigationRequests;
    private Boolean canActuateTrunks;
    private String carSpecialType;
    private String carType;
    private String chargePortType;
    private Boolean copUserSetTempSupported;
    private Boolean dashcamClipSaveSupported;
    private Boolean defaultChargeToMax;
    private String driverAssist;
    private Boolean eceRestrictions;
    private String efficiencyPackage;
    private Boolean euVehicle;
    private String exteriorColor;
    private String exteriorTrim;
    private String exteriorTrimOverride;
    private Boolean hasAirSuspension;
    private Boolean hasLudicrousMode;
    private Boolean hasSeatCooling;
    private String headlampType;
    private String interiorTrimType;
    private Integer keyVersion;
    private Boolean motorizedChargePort;
    private String paintColorOverride;
    private String performancePackage;
    private Boolean plg;
    private Boolean pws;
    private String rearDriveUnit;
    private Integer rearSeatHeaters;
    private Integer rearSeatType;
    private Boolean rhd;
    private String roofColor;
    private String seatType;
    private String spoilerType;
    private String sunRoofInstalled;
    private Boolean supportsQrPairing;
    private String thirdRowSeats;
    private Long timestamp;
    private String trimBadging;
    private Boolean useRangeBadging;
    private Integer utcOffset;
    private Boolean webcamSelfieSupported;
    private Boolean webcamSupported;
    private String wheelType;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class GranularAccess {
    private Boolean hidePrivate;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class VehicleState {
    private Integer apiVersion;
    private String autoparkStateV3;
    private String autoparkStyle;
    private Boolean calendarSupported;
    private String carVersion;
    private Integer centerDisplayState;
    private Boolean dashcamClipSaveAvailable;
    private String dashcamState;
    private Integer df;
    private Integer dr;
    private Integer fdWindow;
    private String featureBitmask;
    private Integer fpWindow;
    private Integer ft;
    private Integer homelinkDeviceCount;
    private Boolean homelinkNearby;
    private Boolean isUserPresent;
    private String lastAutoparkError;
    private Boolean locked;
    private MediaInfo mediaInfo;
    private MediaState mediaState;
    private Boolean notificationsSupported;
    private Double odometer;
    private Boolean parsedCalendarSupported;
    private Integer pf;
    private Integer pr;
    private Integer rdWindow;
    private Boolean remoteStart;
    private Boolean remoteStartEnabled;
    private Boolean remoteStartSupported;
    private Integer rpWindow;
    private Integer rt;
    private Integer santaMode;
    private Boolean sentryMode;
    private Boolean sentryModeAvailable;
    private Boolean serviceMode;
    private Boolean serviceModePlus;
    private Boolean smartSummonAvailable;
    private SoftwareUpdate softwareUpdate;
    private SpeedLimitMode speedLimitMode;
    private Boolean summonStandbyModeEnabled;
    private Long timestamp;
    private Boolean tpmsHardWarningFl;
    private Boolean tpmsHardWarningFr;
    private Boolean tpmsHardWarningRl;
    private Boolean tpmsHardWarningRr;
    private Long tpmsLastSeenPressureTimeFl;
    private Long tpmsLastSeenPressureTimeFr;
    private Long tpmsLastSeenPressureTimeRl;
    private Long tpmsLastSeenPressureTimeRr;
    private Double tpmsPressureFl;
    private Double tpmsPressureFr;
    private Double tpmsPressureRl;
    private Double tpmsPressureRr;
    private Double tpmsRcpFrontValue;
    private Double tpmsRcpRearValue;
    private Boolean tpmsSoftWarningFl;
    private Boolean tpmsSoftWarningFr;
    private Boolean tpmsSoftWarningRl;
    private Boolean tpmsSoftWarningRr;
    private Boolean valetMode;
    private Boolean valetPinNeeded;
    private String vehicleName;
    private Integer vehicleSelfTestProgress;
    private Boolean vehicleSelfTestRequested;
    private Boolean webcamAvailable;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MediaInfo {
      private String a2dpSourceName;
      private Double audioVolume;
      private Double audioVolumeIncrement;
      private Double audioVolumeMax;
      private String mediaPlaybackStatus;
      private String nowPlayingAlbum;
      private String nowPlayingArtist;
      private Integer nowPlayingDuration;
      private Integer nowPlayingElapsed;
      private String nowPlayingSource;
      private String nowPlayingStation;
      private String nowPlayingTitle;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MediaState {
      private Boolean remoteControlEnabled;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SoftwareUpdate {
      private Integer downloadPerc;
      private Integer expectedDurationSec;
      private Integer installPerc;
      private String status;
      private String version;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SpeedLimitMode {
      private Boolean active;
      private Integer currentLimitMph;
      private Integer maxLimitMph;
      private Integer minLimitMph;
      private Boolean pinCodeSet;
    }
  }
}
