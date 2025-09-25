package com.innocito.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.innocito.common.util.ApiUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.innocito.common.constants.AppConstants.COMMA_DELIMITER;
import static com.innocito.common.constants.AppConstants.DOT_DELIMITER;
import static com.innocito.common.constants.AppConstants.DOT_DELIMITER_REGEX;
import static com.innocito.common.constants.AppConstants.HYPHEN_DELIMITER;
import static com.innocito.common.constants.AppConstants.OPEN_SQUARE_BRACKET;
import static com.innocito.common.constants.AppConstants.SPACE_DELIMITER;
import static com.innocito.common.exception.ErrorMessages.CUSTOM_MSG;
import static com.innocito.common.exception.ErrorMessages.MISSING_REQ_PARM;

@Slf4j
@ResponseBody
@RequiredArgsConstructor
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private final ApiUtil apiUtil;

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException exception) {
    log.error(ErrorCode.NOT_FOUND.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND, exception.getMessage());
  }

  @Override
  public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                             HttpHeaders httpHeaders, HttpStatusCode statusCode
    , WebRequest request) {
    String message = ErrorMessages.MALFORMED_JSON_REQUEST;
    Throwable rootCause = exception.getMostSpecificCause();

    if (rootCause instanceof InvalidFormatException) {
      InvalidFormatException invalidFormatException = (InvalidFormatException) rootCause;
      message = String.format(ErrorMessages.INVALID_VALUE_NOT_EXPECTED_TYPE,
        invalidFormatException.getValue(),
        invalidFormatException.getPath().stream()
          .map(reference -> reference.getFieldName() != null
            ? reference.getFieldName() : String.format("[%d]", reference.getIndex()))
          .collect(Collectors.joining(DOT_DELIMITER)),
        invalidFormatException.getTargetType().getSimpleName());
    }
    return new ResponseEntity<>(new ErrorResponseDTO(message, ErrorCode.CONSTRAINT_VIOLATION), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    exception.getBindingResult().getFieldErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      Object value = ((FieldError) error).getRejectedValue();
      if (fieldName.contains(OPEN_SQUARE_BRACKET)) {
        if (fieldName.contains(DOT_DELIMITER)) {
          value = fieldName.split(DOT_DELIMITER_REGEX)[1];
        }
        fieldName = fieldName.split("[\\[\\]]")[0];
        Object errorField = errors.get(fieldName);
        if (ObjectUtils.isNotEmpty(errorField)) {
          if (!errorField.toString().contains(String.valueOf(value))) {
            errors.put(fieldName, value + COMMA_DELIMITER + errors.get(fieldName));
          }
        } else {
          String separator = new StringBuilder().append(SPACE_DELIMITER).append(HYPHEN_DELIMITER)
            .append(SPACE_DELIMITER).toString();
          errors.put(fieldName, value + separator + error.getDefaultMessage());
        }
      } else {
        String message = error.getDefaultMessage();
        errors.put(fieldName, message);
      }
    });
    log.error(ErrorCode.CONSTRAINT_VIOLATION.name() + CUSTOM_MSG + errors);
    return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(ConstraintViolationException exception,
                                                                    WebRequest request) {
    Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
    String errorMessage = StringUtils.EMPTY;
    if (!violations.isEmpty()) {
      ConstraintViolation<?> error = violations.iterator().next();
      if (ObjectUtils.isNotEmpty(error)) {
        String invalidValue = error.getInvalidValue() == null || error.getInvalidValue().toString().isBlank() ?
          StringUtils.EMPTY : error.getInvalidValue().toString();
        errorMessage = invalidValue + SPACE_DELIMITER + error.getMessage();
      }
    }
    log.error(ErrorCode.CONSTRAINT_VIOLATION.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.CONSTRAINT_VIOLATION, errorMessage);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception,
                                                                        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String errorMessage = new StringBuilder(exception.getParameterName()).append(MISSING_REQ_PARM).toString();
    log.error(ErrorCode.INVALID_INPUT.name() + CUSTOM_MSG + errorMessage, exception);
    return new ResponseEntity<>(apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.CONSTRAINT_VIOLATION,
      errorMessage).getBody(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(JsonException.class)
  public ResponseEntity<ErrorResponseDTO> handleJsonException(JsonException exception) {
    log.error(ErrorCode.JSON_EXCEPTION.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.JSON_EXCEPTION, exception.getMessage());
  }

  @ExceptionHandler(LimitConstraintException.class)
  public ResponseEntity<ErrorResponseDTO> handleLimitConstraintException(LimitConstraintException exception) {
    log.error(ErrorCode.CONSTRAINT_VIOLATION.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.CONSTRAINT_VIOLATION, exception.getMessage());
  }

  @ExceptionHandler(ConstraintFailedException.class)
  public ResponseEntity<ErrorResponseDTO> handleConstraintFailedException(ConstraintFailedException exception) {
    log.error(ErrorCode.CONSTRAINT_VIOLATION.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.CONSTRAINT_VIOLATION, exception.getMessage());
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(
    ServletRequestBindingException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    log.error(ErrorCode.CONSTRAINT_VIOLATION.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return new ResponseEntity<>(apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.CONSTRAINT_VIOLATION,
      exception.getMessage()).getBody(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidInputException(InvalidInputException exception) {
    log.error(ErrorCode.INVALID_INPUT.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT, exception.getMessage());
  }

  @ExceptionHandler(SocketTimeoutException.class)
  public ResponseEntity<ErrorResponseDTO> handleSocketTimeoutException(SocketTimeoutException exception) {
    log.error(ErrorCode.SOCKET_TIMEOUT.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.GATEWAY_TIMEOUT, ErrorCode.SOCKET_TIMEOUT, exception.getMessage());
  }

  @ExceptionHandler(TimeOutException.class)
  public ResponseEntity<ErrorResponseDTO> handleTimeOutException(TimeOutException exception) {
    log.error(ErrorCode.TIMEOUT_EXCEPTION.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.GATEWAY_TIMEOUT, ErrorCode.TIMEOUT_EXCEPTION, exception.getMessage());
  }

  @ExceptionHandler(ServerUnavailableException.class)
  public ResponseEntity<ErrorResponseDTO> handleServerUnavailableException(ServerUnavailableException exception) {
    log.error(ErrorCode.SERVER_UNAVAILABLE.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.SERVER_UNAVAILABLE, exception.getMessage());
  }

  @ExceptionHandler(VinServiceException.class)
  public ResponseEntity<ErrorResponseDTO> handleVinServiceException(VinServiceException exception) {
    log.error(ErrorCode.VINSERVICE_EXCEPTION.name() + CUSTOM_MSG + exception.getMessage());
    return apiUtil.buildError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.VINSERVICE_EXCEPTION, exception.getMessage());
  }

  @ExceptionHandler(GraphQlException.class)
  public ResponseEntity<ErrorResponseDTO> handleGraphQlException(GraphQlException exception) {
    log.error(ErrorCode.GRAPHQL_EXCEPTION.name() + CUSTOM_MSG + exception.getMessage(), exception);
    return apiUtil.buildError(HttpStatus.BAD_REQUEST, ErrorCode.GRAPHQL_EXCEPTION, exception.getMessage());
  }
}