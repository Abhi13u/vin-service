package com.innocito.common.exception;

public class GraphQlException extends VinServiceException {
  public GraphQlException(String message) {
    super(message);
  }

  public GraphQlException(String message, Throwable cause) {
    super(message, cause);
  }
}
