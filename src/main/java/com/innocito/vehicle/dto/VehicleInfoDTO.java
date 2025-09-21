package com.innocito.vehicle.dto;

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
public class VehicleInfoDTO {
  private String vehicleId;
  private String vin;
  private String color;
  private String chargingState;
  private Double batteryRange;
  private Double batteryLevel;
  private Double odometer;
  private VehicleCoordinates vehicleCoordinates;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class VehicleCoordinates {
    private Double latitude;
    private Double longitude;
  }

}
