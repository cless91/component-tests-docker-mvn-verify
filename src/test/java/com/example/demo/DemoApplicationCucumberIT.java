package com.example.demo;


import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import io.vavr.control.Try;
import org.awaitility.Awaitility;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
@CucumberContextConfiguration
@ContextConfiguration(initializers = DemoApplicationCucumberIT.Initializer.class)
@ActiveProfiles("componenttest")
public class DemoApplicationCucumberIT {
  @ClassRule
  public static DockerComposeContainer environment =
      new DockerComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
          .withExposedService("app_1", 8080)
          .withExposedService("database_1", 5432);
  private static int applicationPort;
  private static int databasePort;

//  @Container
//  public static DockerComposeContainer environment =
//      new DockerComposeContainer(new File("src/test/resources/docker-compose-test.yml"));

  @Value("${app.url}")
  private String appUrl;
  private int timeoutMillis = 5000;

  @Before
  public void setUp() {
    System.out.println("coucou starting env");
//    environment.start();
  }

  @Given("the application is up")
  public void goToFacebook() {
    System.out.println("appUrl " + appUrl);
    RestTemplate restTemplate = new RestTemplate();
    Awaitility.await().atMost(Duration.ofMillis(timeoutMillis))
        .until(() -> Try.of(() -> restTemplate.getForObject(appUrl + "/actuator/health", String.class)),
            s -> s.getOrElse("nope").equals("{\"status\":\"UP\"}"));
//    String healthResponse = WebClient.create(appUrl).get().uri("/actuator/health").retrieve().bodyToMono(String.class).block(Duration.ofMillis(timeoutMillis));
//    assertThat(healthResponse).isEqualTo("{\"status\":\"UP\"}");
  }

  @And("the database is empty")
  public void theDatabaseIsEmpty() {
  }

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      environment.start();
      applicationPort = environment.getServicePort("app_1", 8080);
      databasePort = environment.getServicePort("database_1", 5432);
      TestPropertyValues.of(
          String.format("spring.datasource.url=jdbc:postgresql://localhost:%d/contacts", databasePort),
          String.format("app.url=http://localhost:%d", applicationPort)
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}