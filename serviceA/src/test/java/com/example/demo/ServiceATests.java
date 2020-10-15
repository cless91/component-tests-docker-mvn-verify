package com.example.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
@SpringBootTest
@Disabled
class ServiceATests {
  @Container
  private static DockerComposeContainer infra =
      new DockerComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
          .waitingFor("database_1", Wait.forLogMessage(".*ready for connections.*", 1));
  @Autowired
  private ContactRepository contactRepository;

  @Test
  void contextLoads() {
    System.out.println("coucou TU");
    ContactJpa contactJpa = new ContactJpa();
    contactJpa.setName("joseph");
    contactRepository.save(contactJpa);
    contactRepository.findAll().forEach(System.out::println);
  }

}
