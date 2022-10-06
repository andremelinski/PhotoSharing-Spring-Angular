package com.springproject.photosharing.global;

import java.util.HashMap;
import java.util.Map;

public class EmailTemplates {

  private static final String staticTemplate =
    "<!DOCTYPE html>\n" +
    "<html>\n" +
    "<head>\n" +
    "    <meta charset=\"utf-8\">\n" +
    "    <title>Example HTML Email</title>\n" +
    "</head>\n" +
    "<body style=\"background: whitesmoke; padding: 30px; height: 100%\">\n" +
    "<h5 style=\"font-size: 18px; margin-bottom: 6px\">Hello All,</h5>\n" +
    "<p style=\"font-size: 16px; font-weight: 500\">This is a simple email using AWS SES</p>\n" +
    "<p><a target=\"_blank\" style=\"background-color: #199319; color: white;padding: 15px 25px; \" href=\"https://www.google.com/\">Google</a></p>" +
    "</body>\n" +
    "</html>";
  private static final String signEmail =
    "<!DOCTYPE html>\n" +
    "<html lang=\"en\">\n" +
    "<head>\n" +
    "    <meta charset=\"utf-8\">\n" +
    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
    "    <title>Thanks for sign in!</title>\n" +
    "</head>\n" +
    "<body style=\"background: whitesmoke; padding: 30px; height: 100%\">\n" +
    "<h5 style=\"font-size: 18px; margin-bottom: 6px\">Hello {{username}},</h5>\n" +
    "<p style=\"font-size: 16px; font-weight: 500\">Thanks for sign in!</p>\n" +
    "<p style=\"font-size: 16px; font-weight: 500\">Your password is : {{password}}</p>\n" +
    "</body>\n" +
    "</html>";
  private static final String changePassword =
    "<!DOCTYPE html>\n" +
    "<html lang=\"en\">\n" +
    "<head>\n" +
    "    <meta charset=\"utf-8\">\n" +
    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
    "    <title>Your password has Changed</title>\n" +
    "</head>\n" +
    "<body style=\"background: whitesmoke; padding: 30px; height: 100%\">\n" +
    "<h5 style=\"font-size: 18px; margin-bottom: 6px\">Hello {{username}},</h5>\n" +
    "<p style=\"font-size: 16px; font-weight: 500\">Your new password is : {{newpassword}}</p>\n" +
    "</body>\n" +
    "</html>";

  public static String templates(String template) {
    Map<String, String> templatesHash = new HashMap<>();
    templatesHash.put("staticTemplate", staticTemplate);
    templatesHash.put("signEmail", signEmail);
    templatesHash.put("changePassword", changePassword);
    return templatesHash.get(template);
  }
}
