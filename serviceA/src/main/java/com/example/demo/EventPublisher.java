package com.example.demo;

import org.springframework.messaging.Message;

public interface EventPublisher {
  void publishEvent(Message<ContactEvent> event);
}
