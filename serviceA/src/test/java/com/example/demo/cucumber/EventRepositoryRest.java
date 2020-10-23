package com.example.demo.cucumber;

import com.example.demo.ContactEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Primary
public class EventRepositoryRest implements EventRepository {
  private RestTemplate restTemplate = new RestTemplate();
  @Value("${kafkalistener.base.url}")
  private String kafkalistenerBaseUrl;

  @Override
  public Optional<ContactEvent> getByCorrelationId(String correlationId) {
    return Optional.ofNullable(restTemplate.getForObject(kafkalistenerBaseUrl+"/getByCorrelationId/"+correlationId,ContactEvent.class));
  }
}
