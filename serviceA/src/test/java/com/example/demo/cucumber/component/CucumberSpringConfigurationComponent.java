package com.example.demo.cucumber.component;

import com.example.demo.ServiceA;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

@SpringBootTest
@CucumberContextConfiguration
@ContextConfiguration(
    classes = {ServiceA.class},
    initializers = CucumberSpringConfigurationComponent.Initializer.class)
@ActiveProfiles("component")
public class CucumberSpringConfigurationComponent {
  @ClassRule
  public static DockerComposeContainer dockerComposeContainer =
      new DockerComposeContainer(new File("src/test/resources/docker-compose-component-test.yml"))
          .withExposedService("app_1", 8080)
          .withExposedService("kafka_1", 9094)
          .withExposedService("kafka_1", 9092)
          .withExposedService("zookeeper_1", 2181)
          .withExposedService("schema-registry_1", 8081)
          .withExposedService("kafka-listener_1", 8080)
          .withExposedService("service-b_1", 8081)
          .withExposedService("database_1", 3306);

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      dockerComposeContainer.start();
      int applicationPort = dockerComposeContainer.getServicePort("app_1", 8080);
      int databasePort = dockerComposeContainer.getServicePort("database_1", 3306);
      int kafkaPort = dockerComposeContainer.getServicePort("kafka_1", 9094);
      int kafkaListenerPort = dockerComposeContainer.getServicePort("kafka-listener_1", 8080);
      int serviceBPort = dockerComposeContainer.getServicePort("service-b_1", 8081);
      TestPropertyValues.of(
          String.format("spring.datasource.url=jdbc:mysql://localhost:%d/contacts?createDatabaseIfNotExist=true&serverTimezone=UTC", databasePort),
          String.format("spring.kafka.bootstrap-servers[0]=http://localhost:%d", kafkaPort ),
          String.format("local.server.port=%d", applicationPort),
          String.format("app.base.url=http://localhost:%d", applicationPort),
          String.format("kafkalistener.base.url=http://localhost:%d", kafkaListenerPort),
          String.format("serviceb.base.url=http://localhost:%d", serviceBPort)
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}
