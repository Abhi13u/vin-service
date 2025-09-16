package com.innocito.rivian;

import com.innocito.rivian.dto.VehicleStateDTO;

public interface RivianVehicleService {
  VehicleStateDTO getVehicleInfoByVin(String vin);
}
