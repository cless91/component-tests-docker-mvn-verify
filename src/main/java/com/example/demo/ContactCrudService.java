package com.example.demo;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class ContactCrudService {

  private final ContactRepository contactRepository;
  private final KafkaTemplate<String, ContactEvent> kafkaTemplate;
  private final String topic;
  private ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .registerModule(new Jdk8Module())
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);

  public ContactRestDto create(ContactRestDto contact, String correlationId, String baseUrl) {
    ContactJpa newContact = contactRepository.save(Mapper.dto2Jpa(contact));
    notifyContactCreated(newContact, correlationId, baseUrl);
    return Mapper.jpa2Dtp(newContact);
  }

  private void notifyContactCreated(ContactJpa contact, String correlationId, String baseUrl) {
    ContactEvent contactCreatedEvent = buildContactCreatedEvent(contact, correlationId);
    Message<ContactEvent> event = MessageBuilder
        .withPayload(contactCreatedEvent)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .setHeader("X-Correlation-ID", correlationId)
        .setHeader("Location", Utils.buildNewContactUrl(baseUrl, contact.getId()))
        .build();
    log.info("sending event to topic {} - {}", topic, contactCreatedEvent.toString());
    kafkaTemplate.send(event);
    kafkaTemplate.flush();
  }

  private ContactEvent buildContactCreatedEvent(ContactJpa contact, String correlationId) {
    ContactEvent contactCreatedEvent = ContactEvent.builder()
        .correlationId(correlationId)
        .eventType(EventType.CONTACT_CREATED)
        .id(contact.getId())
        .attributes(objectMapper.convertValue(contact, Map.class))
        .build();
    return contactCreatedEvent;
  }

}
