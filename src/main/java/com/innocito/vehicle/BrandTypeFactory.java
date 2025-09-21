package com.innocito.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class BrandTypeFactory {
  private final Map<BrandType, BrandTypeHandler> brandTypes =
    Collections.synchronizedMap(new EnumMap<>(BrandType.class));

  @Autowired
  public BrandTypeFactory(Set<BrandTypeHandler> brandTypeHandlers) {
    createBrandTypeHandler(brandTypeHandlers);
  }

  private void createBrandTypeHandler(Set<BrandTypeHandler> brandTypeHandlers) {
    brandTypeHandlers.forEach(
      brandTypeHandler -> brandTypes.put(brandTypeHandler.getBrandType(), brandTypeHandler)
    );
  }

  public BrandTypeHandler findBrandTypeHandler(BrandType brandType) {
    return brandTypes.get(brandType);
  }
}
