package com.innocito.vindecoder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VinDecoderDTO {
  private String make;
  private String model;
  private String trim;
  private Integer modelYear;
  private String vehicleType;
  private String driveType;
  private String gvwr;
  private String wheelSizeFront;
  private String wheelSizeRear;
  private String seats;
  private String color;
  private String evRange;
}