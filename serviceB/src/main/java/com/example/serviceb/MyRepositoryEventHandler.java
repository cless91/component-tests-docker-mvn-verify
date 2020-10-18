package com.example.serviceb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RepositoryEventHandler
@Component
public class MyRepositoryEventHandler {
  @HandleBeforeCreate
  @HandleBeforeSave
  public void handleBeforeSave(MyEntity myEntity) {
    log.info("coucou {}", myEntity);
  }
}
