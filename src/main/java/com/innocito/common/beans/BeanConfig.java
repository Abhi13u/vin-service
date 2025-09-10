package com.innocito.common.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class BeanConfig {

  @Bean(name = "restTemplateWithConnectReadTimeout")
  RestTemplate restTemplateTimeoutWithRequestFactory() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(Duration.ofMillis(5000));
    requestFactory.setReadTimeout(Duration.ofMillis(5000));
    return new RestTemplate(requestFactory);
  }
}