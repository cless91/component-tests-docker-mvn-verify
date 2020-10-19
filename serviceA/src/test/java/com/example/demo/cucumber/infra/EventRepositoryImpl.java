package com.example.demo.cucumber.infra;

import com.example.demo.ContactEvent;
import com.example.demo.cucumber.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class EventRepositoryImpl implements EventRepository {
  List<ContactEvent> receivedEvents = new ArrayList<>();

  @Override
  public Optional<ContactEvent> getByCorrelationId(String correlationId) {
    return receivedEvents.stream()
        .filter(contactEvent -> correlationId.equals(contactEvent.getCorrelationId()))
        .findAny();
  }

  @KafkaListener(topics = "${out.topic}")
  public void listen(ContactEvent event) {
    log.info("received event {}",event.toString());
    receivedEvents.add(event);
  }
}
