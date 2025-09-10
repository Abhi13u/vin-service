package com.innocito.common.exception;

public final class ErrorMessages {
  public static final String OPERATION_NOT_SUPPORTED = "Operation not supported";
  public static final String CONNECTION_URL_INVALID = "Connection URL is invalid: ";
  public static final String SERVER_DOWN = "Server is down, please try again later";
  public static final String NOT_FOUND = "%s not found";
  public static final String MALFORMED_JSON_REQUEST = "Malformed JSON request";
  public static final String JSON_PARSE_ERROR = "Error parsing JSON:";
  public static final String INVALID_VALUE_NOT_EXPECTED_TYPE = "Invalid value '%s' for field '%s'; excepted type is " +
    "'%s'.";
  public static final String CUSTOM_MSG = "\t Custom Exception Message:";
  public static final String MISSING_REQ_PARM = " is required. It cannot be blank";
  public static final String UNEXPECTED_ERROR = "Unexpected error: %s";

  private ErrorMessages() {
    throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
  }
}