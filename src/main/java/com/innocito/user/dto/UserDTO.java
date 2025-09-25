package com.innocito.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
  private String uid;

  @NotBlank(message = "First Name is required, it should not be empty")
  @Size(max = 50, message = "First Name cannot exceed 50 characters")
  private String firstName;

  @NotBlank(message = "Last Name is required, it should not be empty")
  @Size(max = 50, message = "Last Name cannot exceed 50 characters")
  private String lastName;

  @NotBlank(message = "Phone number is required, it should not be empty")
  @Pattern(regexp = "^\\+?[1-9]\\d{6,14}$",
    message = "Invalid phone number format (must be 7-15 digits, may start with +, no leading zeros)")
  private String phoneNumber;

  @NotBlank(message = "Email is required, it should not be empty")
  @Email(message = "Invalid email format")
  private String email;

  @Valid
  @UniqueAddressLabels(message = "Duplicate address labels are not allowed")
  private List<UserAddressDTO> userAddresses;
  private Date createdAt;
  private Date updatedAt;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class UserAddressDTO {
    @NotNull(message = "Label is required, it should not be empty")
    private UserAddressLabel label;

    @NotBlank(message = "Line 1 is required, it should not be empty")
    @Size(max = 255, message = "Line 1 Name cannot exceed 255 characters")
    private String line1;

    @Size(max = 255, message = "Line 2 cannot exceed 50 characters")
    private String line2;

    @NotBlank(message = "City is required, it should not be empty")
    @Size(max = 100, message = "City cannot exceed 50 characters")
    private String city;

    @NotBlank(message = "State is required, it should not be empty")
    @Size(max = 100, message = "State cannot exceed 50 characters")
    private String state;

    @NotBlank(message = "Country is required, it should not be empty")
    @Size(max = 100, message = "Country cannot exceed 50 characters")
    private String country;

    @NotBlank(message = "Postal Code is required, it should not be empty")
    @Size(max = 20, message = "Postal Code cannot exceed 50 characters")
    private String postalCode;

    private Date createdAt;
    private Date updatedAt;
  }
}