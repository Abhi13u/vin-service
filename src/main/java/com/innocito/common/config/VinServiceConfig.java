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
@ConfigurationProperties("vin-service")
public class VinServiceConfig {
  private Contact contact;

  @Data
  @RequiredArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Contact {
    private String title;
    private String url;
    private String email;
    private String description;
    private String team;
    private String license;
    private String terms;
    private String version;
  }
}
