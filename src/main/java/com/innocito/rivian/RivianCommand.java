package com.innocito.rivian;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum RivianCommand {
  WAKE_VEHICLE,
  OPEN_FRUNK,
  CLOSE_FRUNK,
  OPEN_ALL_WINDOWS,
  CLOSE_ALL_WINDOWS,
  UNLOCK_ALL_CLOSURES,
  LOCK_ALL_CLOSURES,
  ENABLE_GEAR_GUARD_VIDEO,
  DISABLE_GEAR_GUARD_VIDEO,
  HONK_AND_FLASH_LIGHTS,
  OPEN_TONNEAU_COVER,
  CLOSE_TONNEAU_COVER;

  @JsonCreator
  public static RivianCommand fromValue(String value) {
    return RivianCommand.valueOf(value.toUpperCase());
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }
}