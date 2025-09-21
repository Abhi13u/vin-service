package com.innocito.vehicle.dto;

import com.innocito.rivian.dto.RivianVehicleStateDTO;
import com.innocito.tesla.dto.TeslaVehicleDataDTO;
import org.springframework.stereotype.Component;

@Component
public class VehicleInfoDTOWrapper {
  public static VehicleInfoDTO buildVehicleInfoDTOFromTesla(TeslaVehicleDataDTO teslaVehicleDataDTO) {
    return VehicleInfoDTO.builder()
      .vehicleId(teslaVehicleDataDTO.getVehicleId())
      .vin(teslaVehicleDataDTO.getVin())
      .color(teslaVehicleDataDTO.getColor())
      .chargingState(teslaVehicleDataDTO.getChargeState().getChargingState())
      .batteryRange(teslaVehicleDataDTO.getChargeState().getBatteryRange())
      .batteryLevel(Double.valueOf(teslaVehicleDataDTO.getChargeState().getBatteryLevel()))
      .odometer(teslaVehicleDataDTO.getVehicleState().getOdometer())
      .vehicleCoordinates(null) // TODO need to get from locationData but structure is not given in doc
      .build();
  }

  public static VehicleInfoDTO buildVehicleInfoDTOFromRivian(RivianVehicleStateDTO rivianVehicleStateDTO) {
    return VehicleInfoDTO.builder()
      .vin(null) // TODO need to see from where we can get - changing query? or by storing vehicleid, vin map
      .color(null)
      .chargingState(rivianVehicleStateDTO.getChargerState().getValue())
      .batteryRange(Double.valueOf(rivianVehicleStateDTO.getDistanceToEmpty().getValue())) // TODO need to see if this is the correct one
      .batteryLevel(rivianVehicleStateDTO.getBatteryLevel().getValue())
      .odometer(Double.valueOf(rivianVehicleStateDTO.getVehicleMileage().getValue()))
      .vehicleCoordinates(VehicleInfoDTO.VehicleCoordinates.builder()
        .latitude(rivianVehicleStateDTO.getGnssLocation().getLatitude())
        .longitude(rivianVehicleStateDTO.getGnssLocation().getLongitude())
        .build())
      .build();
  }
}
