package com.example.demo.infra;

import com.example.demo.ServiceBGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ServiceBGatewayRest implements ServiceBGateway {
  private final RestTemplate restTemplate = new RestTemplate();
  @Value("${serviceb.base.url}")
  private String serviceBBaseUrl;
  @Override
  public String getServiceBValue() {
    Map<String, Object> map = restTemplate.getForObject(serviceBBaseUrl + "/entities/1", Map.class);
    String name = (String) map.get("name");
    return name;
  }
}
