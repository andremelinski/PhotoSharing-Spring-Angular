package com.springproject.photosharing.config;

public class EmailConfig {

  private String from;
  private String to;
  private String Subject;
  private String text;

  /**
   * @return the from
   */
  public String getFrom() {
    return from;
  }

  /**
   * @param from the from to set
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * @return the to
   */
  public String getTo() {
    return to;
  }

  /**
   * @param to the to to set
   */
  public void setTo(String to) {
    this.to = to;
  }

  /**
   * @return the subject
   */
  public String getSubject() {
    return Subject;
  }

  /**
   * @param subject the subject to set
   */
  public void setSubject(String subject) {
    Subject = subject;
  }

  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @param from
   * @param to
   * @param subject
   * @param text
   */
  public EmailConfig(String from, String to, String subject, String text) {
    this.from = from;
    this.to = to;
    Subject = subject;
    this.text = text;
  }
}
