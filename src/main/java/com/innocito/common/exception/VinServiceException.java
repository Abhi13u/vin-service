package com.innocito.common.exception;

public class VinServiceException extends RuntimeException {
  public VinServiceException() {
    super();
  }

  public VinServiceException(String message) {
    super(message);
  }

  public VinServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
