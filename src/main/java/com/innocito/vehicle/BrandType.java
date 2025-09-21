package com.innocito.vehicle;

import com.fasterxml.jackson.annotation.JsonValue;
import com.innocito.common.exception.InvalidConfigurationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.innocito.common.exception.ErrorMessages.NOT_SUPPORTED;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum BrandType {
  TESLA("tesla"),
  RIVIAN("rivian");

  @JsonValue
  @Getter
  private final String code;

  public static BrandType fromValue(String value) {
    return Arrays.stream(BrandType.values())
      .filter(brandType -> value.toLowerCase().contains(brandType.getCode()))
      .findFirst().orElseThrow(() -> new InvalidConfigurationException(String.format(NOT_SUPPORTED, value)));
  }
}