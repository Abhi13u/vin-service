package com.innocito.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateDTO {
  @Size(max = 50, message = "First Name cannot exceed 50 characters")
  private String firstName;

  @Size(max = 50, message = "Last Name cannot exceed 50 characters")
  private String lastName;

  @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number")
  private String phoneNumber;

  @NotBlank(message = "Email is required, it should not be empty")
  @Email(message = "Invalid email format")
  private String email;

  @Valid
  @UniqueAddressLabels(message = "Duplicate address labels are not allowed")
  private List<UserDTO.UserAddressDTO> userAddresses;
}
