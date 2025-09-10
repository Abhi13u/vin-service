package com.innocito.vindecoder;

import com.innocito.vindecoder.dto.VinDecoderDTO;

public interface VinDecoderService {
  VinDecoderDTO getVehicleDetailsByVin(String vin);
}
