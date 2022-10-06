package com.springproject.photosharing.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ResetPasswordDto {

  @NotBlank
  @Email(message = "should be a valid email")
  private String email;

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }
}
