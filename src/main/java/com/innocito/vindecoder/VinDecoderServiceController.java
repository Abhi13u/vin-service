package com.innocito.vindecoder;

import com.innocito.vindecoder.dto.VinDecoderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VinDecoderServiceController {
  @Autowired
  private VinDecoderService vinDecoderService;

  @GetMapping(path = "/vin/decode/{vin}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<VinDecoderDTO> showStation(@PathVariable String vin) {

    return new ResponseEntity<>(vinDecoderService.getVehicleDetailsByVin(vin), HttpStatus.OK);
  }
}
