package com.innocito.vehicle;

import com.innocito.vehicle.dto.SendCommandResponseDTO;
import com.innocito.vehicle.dto.VehicleInfoDTO;
import com.innocito.vindecoder.dto.VinDecoderDTO;

public interface VehicleService {
  VinDecoderDTO getVehicleByVin(String vin);

  VehicleInfoDTO getVehicleInfo(String make, String vehicleID);

  SendCommandResponseDTO sendVehicleCommand(String make, String vehicleID, CommandType command);
}
