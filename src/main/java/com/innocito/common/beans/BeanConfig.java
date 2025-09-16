package com.innocito.common.beans;

import com.innocito.common.config.RivianConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
  @Autowired
  private RivianConfig rivianConfig;

  @Bean(name = "restTemplateWithConnectReadTimeout")
  RestTemplate restTemplateTimeoutWithRequestFactory() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(5000);
    requestFactory.setReadTimeout(5000);
    return new RestTemplate(requestFactory);
  }
}