package com.example.demo.cucumber;

import com.example.demo.ContactEvent;

import java.util.Optional;

public interface EventRepository {
  Optional<ContactEvent> getByCorrelationId(String correlationId);
}
