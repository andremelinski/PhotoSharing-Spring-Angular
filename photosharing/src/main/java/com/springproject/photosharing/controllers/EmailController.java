package com.springproject.photosharing.controllers;

import com.springproject.photosharing.service.EmailService;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class EmailController {

  @Autowired
  private EmailService emailService;

  @PostMapping("/emailtemplate")
  public ResponseEntity<Object> uploadEmailTemplateToSES(
    @RequestBody HashMap<String, String> mapper
  ) {
    try {
      String templateName = mapper.get("templateName");
      String subject = mapper.get("subject");
      String templateCreated =
        this.emailService.createEmailTemplate(templateName, subject);

      return ResponseEntity.status(HttpStatus.OK).body(templateCreated);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }

  @PostMapping("/sendEmail")
  public ResponseEntity<Object> sendEmailBySES(
    @RequestBody HashMap<String, String> request
  ) {
    try {
      String templateName = request.get("templateName");
      String emailReceiver = request.get("emailReceiver");
      JSONObject templateInfoObject =
        this.switchTemplates(templateName, request);
      String templateCreated =
        this.emailService.sendEmail(
            emailReceiver,
            templateName,
            templateInfoObject
          );

      return ResponseEntity.status(HttpStatus.OK).body(templateCreated);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }

  @DeleteMapping("/deleteTemplate")
  public ResponseEntity<Object> deleteTemplateInSES(
    @RequestBody HashMap<String, String> mapper
  ) {
    try {
      String templateName = mapper.get("templateName");
      String templateDeleted =
        this.emailService.deleteEmailTemplate(templateName);

      return ResponseEntity.status(HttpStatus.OK).body(templateDeleted);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }

  private JSONObject switchTemplates(
    String templateName,
    HashMap<String, String> request
  ) {
    JSONObject objTemplateInfo = new JSONObject();

    switch (templateName) {
      case "staticTemplate":
        objTemplateInfo.put("username", "");
        break;
      case "signEmail":
        {
          String username = request.get("username");
          String password = request.get("password");
          objTemplateInfo.put("username", username);
          objTemplateInfo.put("password", password);
        }
        break;
      case "changePassword":
        {
          String username = request.get("username");
          String newpassword = request.get("newpassword");
          objTemplateInfo.put("username", username);
          objTemplateInfo.put("newpassword", newpassword);
        }
        break;
    }

    return objTemplateInfo;
  }
}
