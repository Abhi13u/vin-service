package com.innocito.user.dto;

import com.innocito.user.model.User;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDTOWrapper {
  public static UserDTO buildUserDTOFromUser(User user) {
    List<UserDTO.UserAddressDTO> addressDTOs = new ArrayList<>();
    if (ObjectUtils.isNotEmpty(user.getUserAddresses())) {
      addressDTOs = user.getUserAddresses().stream()
        .map(address -> UserDTO.UserAddressDTO.builder()
          .label(address.getLabel())
          .line1(address.getLine1())
          .line2(address.getLine2())
          .city(address.getCity())
          .state(address.getState())
          .country(address.getCountry())
          .postalCode(address.getPostalCode())
          .createdAt(address.getCreatedAt())
          .updatedAt(address.getUpdatedAt())
          .build())
        .collect(Collectors.toList());
    }

    return UserDTO.builder()
      .uid(user.getUid())
      .firstName(user.getFirstName())
      .lastName(user.getLastName())
      .phoneNumber(user.getPhoneNumber())
      .email(user.getEmail())
      .userAddresses(addressDTOs)
      .createdAt(user.getCreatedAt())
      .updatedAt(user.getUpdatedAt())
      .build();
  }
}
