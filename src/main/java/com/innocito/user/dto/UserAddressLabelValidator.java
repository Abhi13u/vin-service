package com.innocito.user.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserAddressLabelValidator implements ConstraintValidator<UniqueAddressLabels, List<UserDTO.UserAddressDTO>> {

  @Override
  public boolean isValid(List<UserDTO.UserAddressDTO> addresses, ConstraintValidatorContext context) {
    if (CollectionUtils.isEmpty(addresses)) {
      return true;
    }

    Set<UserAddressLabel> existingLabels = new HashSet<>();
    for (UserDTO.UserAddressDTO address : addresses) {
      if (ObjectUtils.isNotEmpty(address.getLabel()) && !existingLabels.add(address.getLabel())) {
        return false;
      }
    }
    return true;
  }
}