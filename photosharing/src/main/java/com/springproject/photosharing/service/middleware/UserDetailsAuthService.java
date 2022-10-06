package com.springproject.photosharing.service.middleware;

import com.springproject.photosharing.model.AppUser;
import com.springproject.photosharing.model.UserRole;
import com.springproject.photosharing.service.AccountService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsAuthService implements UserDetailsService {

  private AccountService accountService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    AppUser appUser = accountService.findByUsername(username);
    if (appUser == null) {
      throw new UsernameNotFoundException(
        "username " + username + " was not found"
      );
    }
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    Set<UserRole> userRoles = appUser.getUserRoles();
    ((Set) userRoles).forEach(
        userRole -> {
          authorities.add(new SimpleGrantedAuthority(userRoles.toString()));
        }
      );
    return new User(appUser.getUsername(), appUser.getPassword(), authorities);
  }
}
