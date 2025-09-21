package com.innocito.rivian;

import com.innocito.rivian.dto.RivianVehicleStateDTO;

public interface RivianVehicleService {
  RivianVehicleStateDTO getVehicleInfoByVin(String vin);
}
