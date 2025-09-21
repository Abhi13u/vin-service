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
@ConfigurationProperties("brand")
public class BrandConfig {
  private RivianConfig rivian;
  private TeslaConfig tesla;

  @Data
  @RequiredArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RivianConfig {
    private String url;
    private String email;
    private String password;
    private String secretKey;
    private String vasPhoneId;
    private String deviceId;
  }

  @Data
  @RequiredArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TeslaConfig {
    private String url;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String audience;
    private String scope;
  }
}
