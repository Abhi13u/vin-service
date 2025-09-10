package com.innocito.common.exception;

public class JsonException extends VinServiceException {
  public JsonException(String message) {
    super(message);
  }

  public JsonException(String message, Throwable cause) {
    super(message, cause);
  }
}
