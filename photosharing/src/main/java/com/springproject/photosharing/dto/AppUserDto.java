package com.springproject.photosharing.dto;

// https://www.baeldung.com/java-bean-validation-not-null-empty-blank
import javax.validation.constraints.*;

public class AppUserDto {

  @NotBlank(message = "Name cannot be null")
  private String name;

  @NotBlank(message = "username cannot be null")
  private String username;

  @NotBlank(message = "email cannot be null")
  @Email(message = "must be a valid email")
  private String email;

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
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

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }
}
