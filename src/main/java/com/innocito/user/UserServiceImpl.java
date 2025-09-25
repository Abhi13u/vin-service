package com.innocito.user;

import com.innocito.common.exception.ErrorMessages;
import com.innocito.common.exception.NotFoundException;
import com.innocito.user.dto.UserAddressLabel;
import com.innocito.user.dto.UserDTO;
import com.innocito.user.dto.UserDTOWrapper;
import com.innocito.user.dto.UserUpdateDTO;
import com.innocito.user.model.User;
import com.innocito.user.model.UserAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDTO registerUser(UserDTO userDTO) {
    User user = User.builder()
      .uid(UUID.randomUUID().toString())
      .firstName(userDTO.getFirstName())
      .lastName(userDTO.getLastName())
      .phoneNumber(userDTO.getPhoneNumber())
      .email(userDTO.getEmail())
      .createdAt(new Date())
      .updatedAt(new Date())
      .build();

    if (CollectionUtils.isNotEmpty(userDTO.getUserAddresses())) {
      Set<UserAddress> addresses = userDTO.getUserAddresses().stream()
        .map(userAddressDTO -> UserAddress.builder()
          .label(userAddressDTO.getLabel())
          .line1(userAddressDTO.getLine1())
          .line2(userAddressDTO.getLine2())
          .city(userAddressDTO.getCity())
          .state(userAddressDTO.getState())
          .country(userAddressDTO.getCountry())
          .postalCode(userAddressDTO.getPostalCode())
          .createdAt(new Date())
          .updatedAt(new Date())
          .user(user)
          .build())
        .collect(Collectors.toSet());
      user.setUserAddresses(addresses);
    }

    userRepository.save(user);
    return UserDTOWrapper.buildUserDTOFromUser(user);
  }

  @Override
  @Transactional
  public UserDTO updateUser(UserUpdateDTO userUpdateDTO) {
    User user = getUserByEmail(userUpdateDTO.getEmail());
    user.setFirstName(ObjectUtils.defaultIfNull(userUpdateDTO.getFirstName(), user.getFirstName()));
    user.setLastName(ObjectUtils.defaultIfNull(userUpdateDTO.getLastName(), user.getLastName()));
    user.setPhoneNumber(ObjectUtils.defaultIfNull(userUpdateDTO.getPhoneNumber(), user.getLastName()));

    if (CollectionUtils.isNotEmpty(userUpdateDTO.getUserAddresses())) {
      Map<UserAddressLabel, UserDTO.UserAddressDTO> addressMap = userUpdateDTO.getUserAddresses()
        .stream()
        .filter(userAddressDTO -> ObjectUtils.isNotEmpty(userAddressDTO.getLabel()))
        .collect(Collectors.toMap(UserDTO.UserAddressDTO::getLabel, userAddressDTO -> userAddressDTO));

      user.getUserAddresses().removeIf(existingAddress -> {
        UserDTO.UserAddressDTO userAddressDTO = addressMap.remove(existingAddress.getLabel());
        if (ObjectUtils.isEmpty(userAddressDTO)) {
          return true;
        }

        existingAddress.setLine1(ObjectUtils.defaultIfNull(userAddressDTO.getLine1(), existingAddress.getLine1()));
        existingAddress.setLine2(ObjectUtils.defaultIfNull(userAddressDTO.getLine2(), existingAddress.getLine2()));
        existingAddress.setCity(ObjectUtils.defaultIfNull(userAddressDTO.getCity(), existingAddress.getCity()));
        existingAddress.setState(ObjectUtils.defaultIfNull(userAddressDTO.getState(), existingAddress.getState()));
        existingAddress.setCountry(ObjectUtils.defaultIfNull(userAddressDTO.getCountry(), existingAddress.getCountry()));
        existingAddress.setPostalCode(ObjectUtils.defaultIfNull(userAddressDTO.getPostalCode(), existingAddress.getPostalCode()));
        existingAddress.setUpdatedAt(new Date());
        return false;
      });

      addressMap.values().forEach(dto -> user.getUserAddresses().add(
        UserAddress.builder()
          .label(dto.getLabel())
          .line1(dto.getLine1())
          .line2(dto.getLine2())
          .city(dto.getCity())
          .state(dto.getState())
          .country(dto.getCountry())
          .postalCode(dto.getPostalCode())
          .createdAt(new Date())
          .updatedAt(new Date())
          .user(user)
          .build()
      ));
    } else {
      user.getUserAddresses().clear();
    }

    user.setUpdatedAt(new Date());
    userRepository.save(user);
    return UserDTOWrapper.buildUserDTOFromUser(user);
  }

  @Override
  @Transactional
  public UserDTO getUser(String id) {
    return UserDTOWrapper.buildUserDTOFromUser(getUserByUID(id));
  }

  private User getUserByUID(String uid) {
    User user = userRepository.findByUid(uid);
    if (ObjectUtils.isEmpty(user)) {
      throw new NotFoundException(String.format(User.class.getSimpleName() + " with uid: " + uid,
        ErrorMessages.NOT_FOUND));
    }
    return user;
  }

  private User getUserByEmail(String email) {
    User user = userRepository.findByEmail(email);
    if (ObjectUtils.isEmpty(user)) {
      throw new NotFoundException(String.format(User.class.getSimpleName() + " with email: " + email,
        ErrorMessages.NOT_FOUND));
    }
    return user;
  }
}
