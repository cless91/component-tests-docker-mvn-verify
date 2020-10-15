package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class AppConfig {
  @Bean
  public ContactCrudService contactCrudService(
      ContactRepository contactRepository,
      KafkaTemplate<String, ContactEvent> kafkaTemplate,
      @Value("${out.topic}") String topic
  ) {
    return new ContactCrudService(contactRepository, kafkaTemplate, topic);
  }
}
