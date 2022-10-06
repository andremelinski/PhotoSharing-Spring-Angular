package com.springproject.photosharing.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.photosharing.model.AppUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthentication extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  public JwtAuthentication(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  // try to auth user
  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  )
    throws AuthenticationException {
    AppUser appuser = null;
    // converting JSON request to appUser object model
    try {
      appuser =
        new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(
        "Unable to convert user from JSON to Java Object"
      );
    }
    return authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        appuser.getUsername(),
        appuser.getPassword()
      )
    );
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authentication
  )
    throws IOException, ServletException {
    User user = (User) authentication.getPrincipal();
    List<String> roles = new ArrayList<>();
    user
      .getAuthorities()
      .forEach(
        authority -> {
          roles.add(authority.getAuthority());
        }
      );
    System.out.println("user " + user);
    System.out.println("roles " + roles);
    String jwtToken = JWT
      .create()
      .withIssuer(request.getRequestURI()) // or "my ocmpany"
      .withSubject(user.getUsername()) // for whom this token is
      .withArrayClaim("roles", roles.stream().toArray(String[]::new)) //passing the roles for this user
      .withExpiresAt(
        new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME) // token expiration
      )
      .sign(Algorithm.HMAC256(SecurityConstants.SECRET)); //
    response.addHeader(
      SecurityConstants.HEADER_TYPE,
      SecurityConstants.TOKEN_PREFIX + jwtToken
    );
  }
}
