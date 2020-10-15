package com.example.demo;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactEvent {
  private String correlationId;
  private EventType eventType;
  private int id;
  private Map<String, Object> attributes;
}
