package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unittest")
class ServiceATests {
  @LocalServerPort
  private int localServerPort;

  @Autowired
  private ContactRepository contactRepository;

  @Test
  void contextLoads() throws InterruptedException {
    System.out.println("coucou TU " + localServerPort);
    Contact contact = new Contact();
    contact.setName("joseph");
    contactRepository.save(contact);
    Thread.sleep(500);
    assertThat(contactRepository.findAll()).anyMatch(c -> "joseph2".equals(c.getName()));
  }

}
