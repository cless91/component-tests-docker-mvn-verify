package com.example.demo.cucumber.unittest;

import com.example.demo.ContactEvent;
import com.example.demo.EventPublisher;
import com.example.demo.cucumber.steps.EventRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Primary
@Profile("unittest")
public class EventRepositoryUnittest implements EventRepository, EventPublisher {
  List<ContactEvent> messages = new ArrayList<>();

  @Override
  public Optional<ContactEvent> getByCorrelationId(String correlationId) {
    return messages.stream().filter(contactEvent -> Objects.equals(contactEvent.getCorrelationId(), correlationId))
        .findAny();
  }

  @Override
  public void publishEvent(Message<ContactEvent> event) {
    messages.add(event.getPayload());
  }
}
