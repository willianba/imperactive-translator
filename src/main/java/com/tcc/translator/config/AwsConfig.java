package com.tcc.translator.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

  @Value("${aws.region}")
  private String REGION;

  @Bean
  public DefaultAWSCredentialsProviderChain credentials() {
    return DefaultAWSCredentialsProviderChain.getInstance();
  }

  @Bean
  public AmazonTranslate translateClient() {
    return AmazonTranslateClient.builder()
      .withCredentials(new AWSStaticCredentialsProvider(credentials().getCredentials()))
      .withRegion(REGION)
      .build();
  }
}
