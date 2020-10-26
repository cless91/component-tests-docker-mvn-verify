package com.example.demo.cucumber.component;

import com.example.demo.ContactEvent;
import com.example.demo.cucumber.steps.EventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Profile("component")
public class EventRepositoryRest implements EventRepository {
  private RestTemplate restTemplate = new RestTemplate();
  @Value("${kafkalistener.base.url}")
  private String kafkalistenerBaseUrl;

  @Override
  public Optional<ContactEvent> getByCorrelationId(String correlationId) {
    return Optional.ofNullable(restTemplate.getForObject(kafkalistenerBaseUrl+"/getByCorrelationId/"+correlationId,ContactEvent.class));
  }
}
