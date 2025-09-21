package com.innocito.common.cache;


import com.innocito.common.config.CacheConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.cache2k.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemoryCachingFactory {
  private Cache<String, Object> memoryCache;

  @Autowired
  public MemoryCachingFactory(CacheConfig cacheConfig) {
    this.memoryCache = cacheConfig.cacheManager();
  }

  public void addToCache(String key, Object value) {
    memoryCache.put(key, value);
  }

  public void addToCache(String key, Object value, long expiryTime) {
    memoryCache.put(key, value);
    memoryCache.expireAt(key, System.currentTimeMillis() + expiryTime);
  }

  public <T> T getFromCache(String key, Class<T> returnType) {
    Object value = memoryCache.get(key);
    if (ObjectUtils.isNotEmpty(value) && returnType.isInstance(value)) {
      return returnType.cast(value);
    }
    return null;
  }

  public void removeFromCache(String key) {
    memoryCache.remove(key);
  }
}
