package com.innocito.vehicle.dto;

import com.innocito.rivian.RivianCommand;
import com.innocito.tesla.TeslaVehicleCommand;
import com.innocito.vehicle.CommandType;
import org.springframework.stereotype.Component;

@Component
public class CommandMapper {
  public static RivianCommand mapToRivian(CommandType commandType) {
    return switch (commandType) {
      case DOOR_LOCK -> RivianCommand.LOCK_ALL_CLOSURES;
      case DOOR_UNLOCK -> RivianCommand.UNLOCK_ALL_CLOSURES;
    };
  }

  public static TeslaVehicleCommand mapToTesla(CommandType commandType) {
    return switch (commandType) {
      case DOOR_LOCK -> TeslaVehicleCommand.DOOR_LOCK;
      case DOOR_UNLOCK -> TeslaVehicleCommand.DOOR_UNLOCK;
    };
  }
}
