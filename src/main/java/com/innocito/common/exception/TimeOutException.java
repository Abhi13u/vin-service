package com.innocito.common.exception;

public class TimeOutException extends VinServiceException {
  public TimeOutException(String message) {
    super(message);
  }

  public TimeOutException(String message, Throwable cause) {
    super(message, cause);
  }
}
