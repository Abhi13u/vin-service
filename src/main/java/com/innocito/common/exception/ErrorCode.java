package com.innocito.common.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {
  NOT_FOUND("not_found"),
  CONSTRAINT_VIOLATION("constraint_violation"),
  JSON_EXCEPTION("json_exception"),
  SOCKET_TIMEOUT("socket_timeout"),
  TIMEOUT_EXCEPTION("timeout_exception"),
  SERVER_UNAVAILABLE("server_unavailable"),
  VINSERVICE_EXCEPTION("vinservice_exception"),
  INVALID_INPUT("invalid_input");

  @JsonValue
  @Getter
  private final String code;
}
