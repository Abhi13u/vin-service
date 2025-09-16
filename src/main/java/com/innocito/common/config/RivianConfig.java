package com.innocito.common.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Configuration
@ConfigurationProperties("rivian")
public class RivianConfig {
  private String url;
  private String email;
  private String password;
}
