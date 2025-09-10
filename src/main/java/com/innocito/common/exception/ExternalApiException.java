package com.innocito.common.exception;

public class ExternalApiException extends VinServiceException {
  public ExternalApiException(String message) {
    super(message);
  }

  public ExternalApiException(String message, Throwable cause) {
    super(message, cause);
  }
}