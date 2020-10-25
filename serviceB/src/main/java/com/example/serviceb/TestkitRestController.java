package com.example.serviceb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
public class TestkitRestController {
  @Autowired
  MyEntityRepository repository;

  @RequestMapping(path = "/given", method = RequestMethod.POST)
  public ResponseEntity<Void> given(@RequestBody MyEntity entity){
    MyEntity savedEntity = repository.save(entity);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("Location", String.valueOf(savedEntity.getId()))
        .build();
  }
}
