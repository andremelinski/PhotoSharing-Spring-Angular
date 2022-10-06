package com.springproject.photosharing.service.interfaces;

import org.json.simple.JSONObject;

public interface IEmailService {
  public String createEmailTemplate(String tempalteName, String subject);

  public String sendEmail(
    String emailReceiver,
    String templateName,
    JSONObject objTemplateInfo
  );

  public String deleteEmailTemplate(String templateName);
}
