package com.innocito.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class GenericResponseErrorHandler implements ResponseErrorHandler {
  private String customMessage;

  public GenericResponseErrorHandler(String customMessage) {
    this.customMessage = customMessage;
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    HttpStatusCode statusCode = clientHttpResponse.getStatusCode();
    if (statusCode.equals(HttpStatus.FORBIDDEN) || statusCode.equals(HttpStatus.NOT_FOUND)) {
      String error = ErrorMessages.CONNECTION_URL_INVALID + clientHttpResponse.getStatusText();
      log.error(error);
      throw new InvalidConfigurationException(error);
    } else if (statusCode.equals(HttpStatus.SERVICE_UNAVAILABLE)) {
      log.error(ErrorMessages.SERVER_DOWN);
      throw new ServerUnavailableException(ErrorMessages.SERVER_DOWN);
    }
  }

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    if (clientHttpResponse.getStatusCode() != HttpStatus.OK) {
      return true;
    }
    return false;
  }

}
