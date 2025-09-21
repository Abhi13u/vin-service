package com.innocito.vehicle;

import com.innocito.vehicle.dto.SendCommandResponseDTO;
import com.innocito.vehicle.dto.VehicleIdDTO;
import com.innocito.vehicle.dto.VehicleInfoDTO;
import com.innocito.vindecoder.VinDecoderService;
import com.innocito.vindecoder.dto.VinDecoderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {
  @Autowired
  BrandTypeFactory brandTypeFactory;

  @Autowired
  VinDecoderService vinDecoderService;

  @Override
  public VinDecoderDTO getVehicleByVin(String vin) {
    VinDecoderDTO vinDecoderDTO = vinDecoderService.getVehicleDetailsByVin(vin);
    vinDecoderDTO.setMake("tesla"); // TODO: just for testing
    VehicleIdDTO vehicleIdDTO =
      brandTypeFactory.findBrandTypeHandler(BrandType.fromValue(vinDecoderDTO.getMake())).getVehicleByVin(vin);
    vinDecoderDTO.setVin(vehicleIdDTO.getVin());
    vinDecoderDTO.setVehicleID(vehicleIdDTO.getVehicleId());
    return vinDecoderDTO;
  }

  @Override
  public VehicleInfoDTO getVehicleInfo(String make, String vehicleID) {
    return brandTypeFactory.findBrandTypeHandler(BrandType.fromValue(make)).getVehicleInfoByID(vehicleID);
  }

  @Override
  public SendCommandResponseDTO sendVehicleCommand(String make, String vehicleID, CommandType command) {
    return brandTypeFactory.findBrandTypeHandler(BrandType.fromValue(make)).sendVehicleCommand(vehicleID, command);
  }
}
