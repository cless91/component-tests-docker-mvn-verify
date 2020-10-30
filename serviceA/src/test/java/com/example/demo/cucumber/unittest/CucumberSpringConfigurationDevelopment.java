package com.example.demo.cucumber.unittest;

import com.example.demo.Contact;
import com.example.demo.ContactRepository;
import com.example.demo.ServiceA;
import com.example.demo.ServiceBGateway;
import io.cucumber.spring.CucumberContextConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {ServiceA.class, CucumberSpringConfigurationDevelopment.Config.class})
@ActiveProfiles("unittest")
class CucumberSpringConfigurationDevelopment {
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

  @Configuration
  public static class Config{
    @Bean
    @Primary
    public ServiceBGateway serviceBGatewayTest(){
      return Mockito.mock(ServiceBGateway.class);
    }
  }
}
