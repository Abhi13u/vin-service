package com.innocito.common.config;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheConfig {
  @Autowired
  VinServiceConfig vinServiceConfig;

  public Cache<String, Object> cacheManager() {
    Cache2kBuilder cache2kBuilder = Cache2kBuilder.of(String.class, Object.class)
      .name(vinServiceConfig.getMemoryCache().getName())
      .entryCapacity(1000)
      .permitNullValues(vinServiceConfig.getMemoryCache().isPermitNullValues());
    return cache2kBuilder.build();
  }
}