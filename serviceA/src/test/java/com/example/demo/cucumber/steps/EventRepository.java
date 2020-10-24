package com.example.demo.cucumber.steps;

import com.example.demo.ContactEvent;

import java.util.Optional;

public interface EventRepository {
  Optional<ContactEvent> getByCorrelationId(String correlationId);
}
