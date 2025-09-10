package com.innocito.common.exception;

public class InvalidInputException extends VinServiceException {
  public InvalidInputException(String message) {
    super(message);
  }

  public InvalidInputException(String message, Throwable cause) {
    super(message, cause);
  }
}
