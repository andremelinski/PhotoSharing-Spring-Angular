package com.springproject.photosharing.dto;

import javax.validation.constraints.*;

public class UpdatePasswordDto {

  @NotBlank
  private String currentPassword;

  @NotBlank
  private String username;

  @NotBlank
  @Min(value = 4, message = "password must have at least 4 characters")
  private String newPassword;

  @NotBlank
  @Min(value = 4, message = "password must have at least 4 characters")
  private String confirmpassword;

  /**
   * @param newPassword the newPassword to set
   */
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  /**
   * @param confirmpassword the confirmpassword to set
   */
  public void setConfirmpassword(String confirmpassword) {
    this.confirmpassword = confirmpassword;
  }

  /**
   * @param currentPassword the currentPassword to set
   */
  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  /**
   * @return the currentPassword
   */
  public String getCurrentPassword() {
    return currentPassword;
  }

  /**
   * @return the newPassword
   */
  public String getNewPassword() {
    return newPassword;
  }

  /**
   * @return the confirmpassword
   */
  public String getConfirmpassword() {
    return confirmpassword;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }
}
