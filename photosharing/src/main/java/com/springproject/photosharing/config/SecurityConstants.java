package com.springproject.photosharing.config;

public class SecurityConstants {

  public static final String SECRET = "secret-token";
  public static final long EXPIRATION_TIME = 432_000_000;
  public static final String HEADER_TYPE = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String CLIENT_DOMAIN_URL = "*";
}
