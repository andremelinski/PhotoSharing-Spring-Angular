package com.springproject.photosharing.dto;

import javax.validation.constraints.NotBlank;

public class SavingPhotoDto {

  @NotBlank(message = "Name cannot be null")
  private String username;

  @NotBlank(message = "caption cannot be null")
  private String caption;

  @NotBlank(message = "location cannot be null")
  private String location;

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
   * @return the caption
   */
  public String getCaption() {
    return caption;
  }

  /**
   * @param caption the caption to set
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(String location) {
    this.location = location;
  }
}
