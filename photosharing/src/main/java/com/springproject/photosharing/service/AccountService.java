package com.springproject.photosharing.service;

import com.springproject.photosharing.global.SES;
import com.springproject.photosharing.model.AppUser;
import com.springproject.photosharing.model.Role;
import com.springproject.photosharing.repo.AppUserRepo;
import com.springproject.photosharing.repo.RoleRepo;
import com.springproject.photosharing.service.interfaces.IAccountService;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class AccountService implements IAccountService {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private AppUserRepo appUserRepo;

  @Autowired
  private RoleRepo roleRepo;

  @Autowired
  private SES ses;

  @Override
  @Transactional
  @Async
  public AppUser saveUser(String name, String username, String email) {
    AppUser appUser = new AppUser();
    String password = RandomStringUtils.randomAlphanumeric(10);
    System.out.println("username: " + username + "password: " + password);
    String encryptedPassword = bCryptPasswordEncoder.encode(password);
    appUser.setPassword(encryptedPassword);
    appUser.setName(name);
    appUser.setUsername(username);
    appUser.setEmail(email);
    appUserRepo.save(appUser);
    JSONObject objTemplateInfo = new JSONObject();
    objTemplateInfo.put("username", username);
    objTemplateInfo.put("password", password);
    ses.sendEmail(email, "signEmail", objTemplateInfo);
    return appUser;
  }

  @Override
  public AppUser findByUsername(String username) {
    return appUserRepo.findByUsername(username);
  }

  @Override
  public AppUser findByEmail(String userEmail) {
    return appUserRepo.findByEmail(userEmail);
  }

  @Override
  public List<AppUser> userList() {
    return appUserRepo.findAll();
  }

  @Override
  public Role findUserRoleByName(String roleName) {
    return roleRepo.findRoleByName(roleName);
  }

  @Override
  public Role saveRole(Role role) {
    return roleRepo.save(role);
  }

  @Override
  @Transactional
  public String updateUserPassword(AppUser appUser, String newpassword) {
    String encryptedPassword = bCryptPasswordEncoder.encode(newpassword);
    appUser.setPassword(encryptedPassword);
    appUserRepo.save(appUser);
    System.out.println("updateUserPassword " + appUser);
    return newpassword;
  }

  @Override
  @Transactional
  public AppUser updateUser(AppUser user, HashMap<String, String> request) {
    String name = ((name = request.get("name")) != null)
      ? name
      : user.getName();
    String email = ((email = request.get("email")) != null)
      ? email
      : user.getName();
    String bio = ((bio = request.get("bio")) != null) ? bio : user.getName();
    user.setName(name);
    user.setEmail(email);
    user.setBio(bio);
    appUserRepo.save(user);
    return user;
  }

  @Override
  @Transactional
  public AppUser simpleSaveUser(AppUser user) {
    appUserRepo.save(user);
    return user;
  }

  @Override
  public AppUser findUserById(Long id) {
    return appUserRepo.findUserById(id);
  }

  @Override
  @Transactional
  public void deleteUser(AppUser appUser) {
    appUserRepo.delete(appUser);
    System.out.println("deleteUser " + appUser);
  }

  @Override
  @Transactional
  public void resetPassword(AppUser user) {
    String password = RandomStringUtils.randomAlphanumeric(10);
    String encryptedPassword = bCryptPasswordEncoder.encode(password);
    user.setPassword(encryptedPassword);
    AppUser newUserInfo = appUserRepo.save(user);
    System.out.println("AccountService.resetPassword()" + newUserInfo);
  }

  @Override
  public List<AppUser> getUsersListByUsername(String username) {
    return appUserRepo.findByUsernameContaining(username);
  }

  @Override
  public String saveUserImage(MultipartFile multipartFile, Long userImageId) {
    // TODO Auto-generated method stub
    return "saveUserImage";
  }
}
