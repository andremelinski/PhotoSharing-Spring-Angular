package com.springproject.photosharing.service;

import com.springproject.photosharing.global.SES;
import com.springproject.photosharing.service.interfaces.IEmailService;
import javax.transaction.Transactional;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailService implements IEmailService {

  @Autowired
  private SES ses;

  @Override
  @Async
  public String createEmailTemplate(String tempalteName, String subject) {
    if (tempalteName.isBlank() || subject.isBlank()) {
      throw new IllegalStateException(
        "template name or subject van not be blank"
      );
    }
    try {
      return ses.createTemplate(tempalteName, subject);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  @Async
  public String sendEmail(
    String emailReceiver,
    String templateName,
    JSONObject objTemplateInfo
  ) {
    if (emailReceiver.isBlank() || templateName.isBlank()) {
      throw new IllegalStateException(
        "template name or subject van not be blank"
      );
    }
    try {
      return ses.sendEmail(emailReceiver, templateName, objTemplateInfo);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  @Async
  public String deleteEmailTemplate(String templateName) {
    if (templateName.isBlank()) {
      throw new IllegalStateException(
        "template name or subject van not be blank"
      );
    }
    try {
      return ses.deleteTemplate(templateName);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
