package com.example.demo.testlistener;

import com.example.demo.ContactEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@Slf4j
public class KafkaListenerEntrypoint {

  public static void main(String[] args) {
    SpringApplication.run(KafkaListenerEntrypoint.class, args);
  }

  List<ContactEvent> receivedEvents = new ArrayList<>();

  @GetMapping("/getByCorrelationId/{correlationId}")
  public Optional<ContactEvent> getByCorrelationId(@PathVariable("correlationId") String correlationId) {
    return receivedEvents.stream()
        .filter(contactEvent -> correlationId.equals(contactEvent.getCorrelationId()))
        .findAny();
  }

  @KafkaListener(topics = "${out.topic}")
  public void listen(ContactEvent event) {
    log.info("received event {}", event.toString());
    receivedEvents.add(event);
  }
}
