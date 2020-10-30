package com.example.demo.infra;

import com.example.demo.ContactEvent;
import com.example.demo.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherKafka implements EventPublisher {
  private final KafkaTemplate<String, ContactEvent> kafkaTemplate;
  @Override
  public void publishEvent(Message<ContactEvent> event) {
    kafkaTemplate.send(event);
  }
}
