package com.innocito.common.constants;

import com.innocito.common.exception.ErrorMessages;

public final class AppConstants {
  public static final String SPACE_DELIMITER = " ";
  public static final String DOT_DELIMITER = ".";
  public static final String DOT_DELIMITER_REGEX = "\\.";
  public static final String OPEN_SQUARE_BRACKET = "[";
  public static final String COMMA_DELIMITER = ",";
  public static final String HYPHEN_DELIMITER = "-";
  public static final String RESULTS = "Results";

  private AppConstants() {
    throw new UnsupportedOperationException(ErrorMessages.OPERATION_NOT_SUPPORTED);
  }
}
