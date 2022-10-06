package com.springproject.photosharing.global;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class SES {

  @Value("${amazonProperties.email}")
  private String senderEmail;

  @Autowired
  public AmazonSimpleEmailService amazonSimpleEmailService;

  @Autowired
  ObjectMapper objectMapper;

  // public String[] to = { "krpiyush48@gmail.com" };
  public String sendEmail(
    String emailReceiver,
    String templateName,
    JSONObject objTemplateInfo
  ) {
    // String templateData = "{ \"username\":\"Jack\", \"password\": \"Tiger\"}";
    Destination destination = new Destination();
    List<String> toAddresses = new ArrayList<String>();
    toAddresses.add(emailReceiver);

    // String[] Emails = to;
    // for (String email : Emails) {
    //   toAddresses.add(email);
    // }
    String templateData = objTemplateInfo.toJSONString();
    destination.setToAddresses(toAddresses);
    SendTemplatedEmailRequest templatedEmailRequest = new SendTemplatedEmailRequest();
    templatedEmailRequest.setDestination(destination);
    templatedEmailRequest.withTemplate(templateName);
    if (!templateName.equals("staticTemplate")) {
      templatedEmailRequest.withTemplateData(templateData);
    }
    templatedEmailRequest.withSource(senderEmail);
    amazonSimpleEmailService.sendTemplatedEmail(templatedEmailRequest);
    return "email sent";
  }

  public String createTemplate(String templateName, String subject)
    throws NoSuchFieldException, SecurityException {
    String emailTemplate = EmailTemplates.templates(templateName);
    String emailContent = emailTemplate.toString();
    Template sesTemplate = new Template();
    sesTemplate.setTemplateName(templateName);
    sesTemplate.setSubjectPart(subject);
    sesTemplate.setHtmlPart(emailContent);
    CreateTemplateRequest request = new CreateTemplateRequest();
    request.setTemplate(sesTemplate);
    amazonSimpleEmailService.createTemplate(request);
    return templateName + "'s Template Created";
  }

  public String deleteTemplate(String templateName) {
    DeleteTemplateRequest request = new DeleteTemplateRequest();
    request.setTemplateName(templateName);
    amazonSimpleEmailService.deleteTemplate(request);
    return templateName + " Deleted";
  }
}
