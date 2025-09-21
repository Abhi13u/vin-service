package com.innocito.vehicle;

import com.innocito.vehicle.dto.SendCommandResponseDTO;
import com.innocito.vehicle.dto.VehicleInfoDTO;
import com.innocito.vindecoder.dto.VinDecoderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleController {
  @Autowired
  private VehicleService vehicleService;

  @GetMapping(path = "/vehicle/{vin}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<VinDecoderDTO> getVehicleByVin(@PathVariable String vin) {
    return new ResponseEntity<>(vehicleService.getVehicleByVin(vin), HttpStatus.OK);
  }

  @GetMapping(path = "/vehicle/{make}/{vehicleID}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<VehicleInfoDTO> getVehicleInfo(@PathVariable String make, @PathVariable String vehicleID) {
    // TODO : can remove make, if we store vin, id, make mapping. or instead of make, pass vin
    return new ResponseEntity<>(vehicleService.getVehicleInfo(make, vehicleID), HttpStatus.OK);
  }

  @PostMapping(path = "/vehicle/{make}/{vehicleID}/command/{command}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SendCommandResponseDTO> sendVehicleCommand(@PathVariable String make,
                                                                   @PathVariable String vehicleID,
                                                                   @PathVariable CommandType command) {
    return new ResponseEntity<>(vehicleService.sendVehicleCommand(make, vehicleID, command), HttpStatus.OK);
  }
}
