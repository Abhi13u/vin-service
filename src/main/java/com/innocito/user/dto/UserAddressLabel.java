package com.innocito.user.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.innocito.common.exception.InvalidConfigurationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.innocito.common.exception.ErrorMessages.NOT_SUPPORTED;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum UserAddressLabel {
  HOME,
  OFFICE;

  // TODO: if this is not fixed, we can just take from input

  public static UserAddressLabel fromValue(String value) {
    return Arrays.stream(UserAddressLabel.values())
      .filter(userAddressLabel -> value != null && value.toUpperCase().contains(userAddressLabel.name()))
      .findFirst()
      .orElseThrow(() -> new InvalidConfigurationException(String.format(NOT_SUPPORTED, value)));
  }

  @JsonValue
  public String getValue() {
    return name().toLowerCase();
  }
}
