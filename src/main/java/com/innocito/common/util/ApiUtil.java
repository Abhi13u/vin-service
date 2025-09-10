package com.innocito.common.util;

import com.innocito.common.exception.ErrorCode;
import com.innocito.common.exception.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiUtil {
  public ResponseEntity<ErrorResponseDTO> buildError(HttpStatus status, ErrorCode errorCode, String message) {
    return buildError(status, message, errorCode);
  }

  public ResponseEntity<ErrorResponseDTO> buildError(HttpStatus status, String message) {
    return buildError(status, message, null);
  }

  public ResponseEntity<ErrorResponseDTO> buildError(HttpStatus status, String message, ErrorCode errorCode) {
    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(message, errorCode);
    return new ResponseEntity<>(errorResponseDTO, status);
  }
}