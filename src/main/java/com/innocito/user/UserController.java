package com.innocito.user;

import com.innocito.user.dto.UserDTO;
import com.innocito.user.dto.UserUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping(path = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
    return new ResponseEntity<>(userService.registerUser(userDTO), HttpStatus.CREATED);
  }

  @PutMapping(path = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
    MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
    return new ResponseEntity<>(userService.updateUser(userUpdateDTO), HttpStatus.OK);
  }

  @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
    // TODO: we can also get by email if needed
    return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
  }
}
