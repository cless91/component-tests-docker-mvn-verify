package com.example.demo.cucumber;

import com.example.demo.cucumber.steps.ServiceBConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Profile("! unittest")
public class ServiceBConfigurerRest implements ServiceBConfigurer {

  private String serviceBBaseUrl;
  private final RestTemplate restTemplate = new RestTemplate();

  public ServiceBConfigurerRest(@Value("${serviceb.base.url}") String serviceBBaseUrl) {
    this.serviceBBaseUrl = serviceBBaseUrl;
  }

  @Override
  public void configureResponse(String data) {
    Map<String, Object> request = new HashMap<>();
    request.put("name", data);
    log.info("posting test data to service-b on url: {}", serviceBBaseUrl);
    restTemplate.postForEntity(serviceBBaseUrl + "/given", request, Void.TYPE);
  }
}
