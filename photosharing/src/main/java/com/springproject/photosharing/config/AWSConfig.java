package com.springproject.photosharing.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

  @Value("${amazonProperties.Region}")
  private Regions region;

  @Value("${amazonProperties.accessKey}")
  private String accessKey;

  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  @Bean
  public AmazonS3 s3() {
    AWSCredentials aWSCredentials = new BasicAWSCredentials(
      accessKey,
      secretKey
    );
    return AmazonS3ClientBuilder
      .standard()
      .withRegion(region)
      .withCredentials(new AWSStaticCredentialsProvider(aWSCredentials))
      .build();
  }

  @Bean
  public AmazonSimpleEmailService amazonSimpleEmailService() {
    AWSCredentials aWSCredentials = new BasicAWSCredentials(
      accessKey,
      secretKey
    );
    return AmazonSimpleEmailServiceClientBuilder
      .standard()
      .withRegion(region)
      .withCredentials(new AWSStaticCredentialsProvider(aWSCredentials))
      .build();
  }
}
