package com.innocito.common.exception;

public class ServerUnavailableException extends VinServiceException {
  public ServerUnavailableException(String message) {
    super(message);
  }

  public ServerUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
