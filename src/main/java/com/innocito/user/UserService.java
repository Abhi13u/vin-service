package com.innocito.user;

import com.innocito.user.dto.UserDTO;
import com.innocito.user.dto.UserUpdateDTO;

public interface UserService {
  UserDTO registerUser(UserDTO userDTO);

  UserDTO updateUser(UserUpdateDTO userUpdateDTO);

  UserDTO getUser(String id);
}
