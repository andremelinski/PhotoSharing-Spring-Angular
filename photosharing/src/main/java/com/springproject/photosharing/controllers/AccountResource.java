package com.springproject.photosharing.controllers;

import com.springproject.photosharing.dto.AppUserDto;
import com.springproject.photosharing.dto.ResetPasswordDto;
import com.springproject.photosharing.dto.UpdatePasswordDto;
import com.springproject.photosharing.model.AppUser;
import com.springproject.photosharing.service.AccountService;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AccountResource {

  @Autowired
  private AccountService accountService;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/list")
  public ResponseEntity<Object> getUserList() {
    List<AppUser> users = accountService.userList();
    if (users.isEmpty()) {
      return new ResponseEntity<>("No users found", HttpStatus.OK);
    }
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @GetMapping("/{username}")
  public ResponseEntity<Object> getUserInfo(
    @PathVariable("username") String username
  ) {
    AppUser user = accountService.findByUsername(username);
    if (user == null) {
      return new ResponseEntity<>("User not found", HttpStatus.OK);
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/findByUsername/{username}")
  public ResponseEntity<Object> getUsersListByUserName(
    @PathVariable("username") String username
  ) {
    List<AppUser> users = accountService.getUsersListByUsername(username);
    if (users.isEmpty()) {
      return new ResponseEntity<>("No users found", HttpStatus.OK);
    }
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @PostMapping("/register")
  public ResponseEntity<Object> register(
    // @RequestBody HashMap<String, String> request
    @RequestBody @Valid AppUserDto parkingSpotDto
  ) {
    String username = parkingSpotDto.getUsername();
    String email = parkingSpotDto.getEmail();

    if (accountService.findByEmail(email) != null) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Conflict: email already exist!");
    }
    if (accountService.findByUsername(username) != null) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Conflict: Username already exist!");
    }
    String name = parkingSpotDto.getName();
    try {
      AppUser user = accountService.saveUser(name, username, email);
      return ResponseEntity.status(HttpStatus.OK).body(user);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Conflict:An error accour: " + e.toString());
    }
  }

  @PutMapping("/udpate")
  public ResponseEntity<Object> registrater(
    @RequestBody HashMap<String, String> request
  ) {
    String id = request.get("id");
    AppUser user = accountService.findUserById(Long.parseLong(id));
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("userNotFound");
    }
    try {
      AppUser newUserInfo = accountService.updateUser(user, request);
      return ResponseEntity.status(HttpStatus.OK).body(newUserInfo);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Conflict:An error accour: " + e);
    }
  }

  @PostMapping("/changePassword")
  public ResponseEntity<String> changePassword(
    @RequestBody @Valid UpdatePasswordDto updatePasswordDto
  ) {
    String username = updatePasswordDto.getUsername();
    AppUser appUser = accountService.findByUsername(username);
    if (appUser == null) {
      return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
    }
    String currentPassword = updatePasswordDto.getCurrentPassword();
    String newPassword = updatePasswordDto.getNewPassword();
    String confirmpassword = updatePasswordDto.getConfirmpassword();
    if (!newPassword.equals(confirmpassword)) {
      return new ResponseEntity<>("PasswordNotMatched", HttpStatus.BAD_REQUEST);
    }
    String userPassword = appUser.getPassword();
    String passwordUpdated = "";
    try {
      if (
        newPassword != null &&
        !newPassword.isEmpty() &&
        !ObjectUtils.isEmpty(newPassword)
      ) {
        if (bCryptPasswordEncoder.matches(newPassword, userPassword)) {
          return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("SamePasswordAsBefore");
        }
        if (bCryptPasswordEncoder.matches(currentPassword, userPassword)) {
          passwordUpdated =
            accountService.updateUserPassword(appUser, newPassword);
        }
      } else {
        return new ResponseEntity<>(
          "IncorrectCurrentPassword",
          HttpStatus.BAD_REQUEST
        );
      }
      return new ResponseEntity<>(passwordUpdated, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
        "Error Occured: " + e.getMessage(),
        HttpStatus.BAD_REQUEST
      );
    }
  }

  @PutMapping("/resetPassword/")
  public ResponseEntity<String> resetPassword(
    @RequestBody @Valid ResetPasswordDto resetPasswordDto
  ) {
    String email = resetPasswordDto.getEmail();
    AppUser user = accountService.findByEmail(email);
    if (user == null) {
      return new ResponseEntity<String>(
        "emailNotFound",
        HttpStatus.BAD_REQUEST
      );
    }
    try {
      accountService.resetPassword(user);
      return new ResponseEntity<String>("EmailSent!", HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Conflict:An error accour: " + e);
    }
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteUser(
    @RequestBody HashMap<String, String> mapper
  ) {
    String username = mapper.get("username");
    AppUser user = accountService.findByUsername(username);
    try {
      accountService.deleteUser(user);
      return new ResponseEntity<String>(
        "User Deleted Successfully!",
        HttpStatus.OK
      );
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Conflict:An error accour: " + e);
    }
  }
}
