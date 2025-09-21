package com.innocito.rivian;

import com.innocito.rivian.dto.RivianVehicleStateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rivian")
public class RivianVehicleController {
  @Autowired
  RivianVehicleService rivianVehicleService;

  @GetMapping(path = "/vehicleinfo/{vin}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RivianVehicleStateDTO> getVehicleInfoByVin(@PathVariable String vin) {

    return new ResponseEntity<>(rivianVehicleService.getVehicleInfoByVin(vin), HttpStatus.OK);
  }
}
