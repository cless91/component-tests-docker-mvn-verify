package com.example.demo.cucumber;

import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class TestContext {
  private static String correlationId;

  public static String getCorrelationId() {
    return correlationId;
  }

  @Before
  public void setUp() {
    correlationId = UUID.randomUUID().toString();
    log.info("correlation id : {}", correlationId);
  }
}
