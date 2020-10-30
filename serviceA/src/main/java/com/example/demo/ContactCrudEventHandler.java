package com.example.demo;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RepositoryEventHandler
@Component
public class ContactCrudEventHandler {

  private final KafkaTemplate<String, ContactEvent> kafkaTemplate;
  @Value("${out.topic}")
  private String topic;
  private final ServiceBGateway serviceBGateway;
  private final HttpServletRequest request;
  private ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .registerModule(new Jdk8Module())
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);

  @HandleBeforeCreate
  public void queryServiceBBeforeCreate(Contact contact) {
    String name = serviceBGateway.getServiceBValue();
    contact.setOtherValue(name);
  }

  @HandleAfterCreate
  public void notifyContactCreated(Contact contact) {
    String correlationId = getCorrelationId();
    ContactEvent contactCreatedEvent = buildContactCreatedEvent(contact, correlationId);
    String locationHeader = buildCreatedLocationHeader(contact);
    Message<ContactEvent> event = MessageBuilder
        .withPayload(contactCreatedEvent)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .setHeader("X-Correlation-ID", correlationId)
        .setHeader("Location", locationHeader)
        .build();
    log.info("sending event to topic {} - {}", topic, contactCreatedEvent.toString());
    kafkaTemplate.send(event);
  }

  private String getCorrelationId() {
    String requestCorrelationId = request.getHeader("X-Correlation-ID");
    String correlationId;
    if (requestCorrelationId == null) {
      correlationId = UUID.randomUUID().toString();
      log.debug("no correlation id on request, creating one, {}", correlationId);
    } else {
      correlationId = requestCorrelationId;
    }
    return correlationId;
  }

  private String buildCreatedLocationHeader(Contact contact) {
    return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + request.getServletPath() + "/" + contact.getId();
  }

  private String buildAlteredLocationHeader() {
    return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + request.getServletPath();
  }

  private ContactEvent buildContactCreatedEvent(Contact contact, String correlationId) {
    ContactEvent contactCreatedEvent = ContactEvent.builder()
        .correlationId(correlationId)
        .eventType(EventType.CONTACT_CREATED)
        .id(contact.getId())
        .attributes(objectMapper.convertValue(contact, Map.class))
        .build();
    return contactCreatedEvent;
  }

  @HandleAfterSave
  public void notifyContactUpdated(Contact contact) {
    String correlationId = getCorrelationId();
    ContactEvent contactUpdatedEvent = buildContactUpdatedEvent(contact, correlationId);
    String locationHeader = buildAlteredLocationHeader();
    Message<ContactEvent> event = MessageBuilder
        .withPayload(contactUpdatedEvent)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .setHeader("X-Correlation-ID", correlationId)
        .setHeader("Location", locationHeader)
        .build();
    log.info("sending event to topic {} - {}", topic, contactUpdatedEvent.toString());
    kafkaTemplate.send(event);
  }

  private ContactEvent buildContactUpdatedEvent(Contact contact, String correlationId) {
    ContactEvent contactUpdatedEvent = ContactEvent.builder()
        .correlationId(correlationId)
        .eventType(EventType.CONTACT_UPDATED)
        .id(contact.getId())
        .attributes(objectMapper.convertValue(contact, Map.class))
        .build();
    return contactUpdatedEvent;
  }

  @HandleAfterDelete
  public void notifyContactDeleted(Contact contact) {
    String correlationId = getCorrelationId();
    ContactEvent contactUpdatedEvent = buildContactDeletedEvent(contact.getId(), correlationId);
    String locationHeader = buildAlteredLocationHeader();
    Message<ContactEvent> event = MessageBuilder
        .withPayload(contactUpdatedEvent)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .setHeader("X-Correlation-ID", correlationId)
        .setHeader("Location", locationHeader)
        .build();
    log.info("sending event to topic {} - {}", topic, contactUpdatedEvent.toString());
    kafkaTemplate.send(event);
  }

  private ContactEvent buildContactDeletedEvent(int contactId, String correlationId) {
    ContactEvent contactUpdatedEvent = ContactEvent.builder()
        .correlationId(correlationId)
        .eventType(EventType.CONTACT_DELETED)
        .id(contactId)
        .build();
    return contactUpdatedEvent;
  }
}
