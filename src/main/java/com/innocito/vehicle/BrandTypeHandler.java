package com.innocito.vehicle;

import com.innocito.vehicle.dto.SendCommandResponseDTO;
import com.innocito.vehicle.dto.VehicleIdDTO;
import com.innocito.vehicle.dto.VehicleInfoDTO;

public interface BrandTypeHandler {
  BrandType getBrandType();

  VehicleIdDTO getVehicleByVin(String vin);

  VehicleInfoDTO getVehicleInfoByID(String vehicleID);

  SendCommandResponseDTO sendVehicleCommand(String vehicleID, CommandType commandType);
}
