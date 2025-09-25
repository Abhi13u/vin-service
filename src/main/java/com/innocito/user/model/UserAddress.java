package com.innocito.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.innocito.user.dto.UserAddressLabel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldNameConstants
@Table(name = "user_address")
public class UserAddress {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_address")
  @SequenceGenerator(
    name = "seq_user_address", sequenceName = "user_address_id_seq", allocationSize = 1
  )
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "label")
  @Enumerated(EnumType.STRING)
  private UserAddressLabel label;

  @Column(name = "line_1")
  private String line1;

  @Column(name = "line_2")
  private String line2;

  @Column(name = "city")
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "country")
  private String country;

  @Column(name = "postal_code")
  private String postalCode;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;


  @Override
  public boolean equals(Object object) {
    if (ObjectUtils.isEmpty(object)) {
      return false;
    }
    if (!(object instanceof UserAddress userAddress)) {
      return false;
    }
    return Objects.equals(getId(), userAddress.getId());
  }

  @Override
  public int hashCode() {
    if (id == null) {
      return System.identityHashCode(this);
    }
    return Long.hashCode(getId());
  }
}
