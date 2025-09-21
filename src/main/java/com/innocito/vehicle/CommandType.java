package com.innocito.vehicle;

import com.fasterxml.jackson.annotation.JsonValue;
import com.innocito.common.exception.InvalidConfigurationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.innocito.common.exception.ErrorMessages.NOT_SUPPORTED;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum CommandType {
  DOOR_LOCK,
  DOOR_UNLOCK;

  public static CommandType fromValue(String value) {
    return Arrays.stream(CommandType.values())
      .filter(commandType -> value != null && value.toUpperCase().contains(commandType.name()))
      .findFirst()
      .orElseThrow(() -> new InvalidConfigurationException(String.format(NOT_SUPPORTED, value)));
  }

  @JsonValue
  public String getValue() {
    return name().toLowerCase();
  }
}