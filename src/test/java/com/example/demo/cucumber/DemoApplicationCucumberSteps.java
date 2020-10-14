package com.example.demo.cucumber;


import com.example.demo.ContactJpa;
import com.example.demo.ContactRepository;
import com.example.demo.ContactRestDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.vavr.control.Try;
import org.awaitility.Awaitility;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@CucumberContextConfiguration
@ContextConfiguration(initializers = DemoApplicationCucumberSteps.Initializer.class)
@ActiveProfiles("componenttest")
public class DemoApplicationCucumberSteps {
  @ClassRule
  public static DockerComposeContainer environment =
      new DockerComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
          .withExposedService("app_1", 8080)
          .withExposedService("database_1", 3306);
  private static int applicationPort;
  private static int databasePort;
  @Autowired
  private ContactRepository contactRepository;
  @Value("${app.base.url}")
  private String appBaseUrl;
  private int timeoutMillis = 7000;
  private final RestTemplate restTemplate = new RestTemplate();
  private ResponseEntity<Void> actualCreateContactResponseEntity;
  private ResponseEntity<Void> actualUpdateContactResponseEntity;
  int idNewContact;

  @Given("the application is up and ready")
  public void goToFacebook() {
    System.out.println("appUrl " + appBaseUrl);
    Awaitility.await().atMost(Duration.ofMillis(timeoutMillis))
        .pollInterval(Duration.ofSeconds(1))
        .with()
        .conditionEvaluationListener(evaluatedCondition -> System.out.println(evaluatedCondition.getValue().toString()))
        .until(
            () -> Try.of(() -> restTemplate.getForObject(appBaseUrl + "/actuator/health", ApplicationStatus.class)).toJavaOptional(),
            statusOpt ->
                Objects.equals(statusOpt.map(ApplicationStatus::getStatus).orElse(null), "UP")
                    && Objects.equals(statusOpt.map(ApplicationStatus::getDatabaseStatus).orElse(null), "UP")
        );
  }

  @And("the database is empty")
  public void theDatabaseIsEmpty() {
    contactRepository.deleteAll();
    Iterable<ContactJpa> allContacts = contactRepository.findAll();
    assertThat(allContacts).isEmpty();
  }

  @When("the following \"CREATE CONTACT\" REST request is sent:")
  public void theFollowingCreateContactRESTRequestIsSent(ContactRestDto contactRestDto) {
    actualCreateContactResponseEntity = restTemplate.postForEntity(appBaseUrl + "/contact", contactRestDto, Void.TYPE);
  }

  @When("the following \"UPDATE CONTACT\" REST request is sent:")
  public void theFollowingUpdateContactRESTRequestIsSent(ContactRestDto contactRestDto) {
    actualUpdateContactResponseEntity = restTemplate.exchange(appBaseUrl + "/contact/" + idNewContact,
        HttpMethod.PUT,
        new HttpEntity<>(contactRestDto),
        Void.TYPE);
  }

  @When("a \"DELETE CONTACT\" REST request is sent")
  public void aDeleteContactRESTRequestIsSent() {
    actualUpdateContactResponseEntity = restTemplate.exchange(appBaseUrl + "/contact/" + idNewContact,
        HttpMethod.DELETE,
        null,
        Void.TYPE);
  }

  @Then("the following contact is present in the database:")
  public void theFollowingContactIsPresentInTheDatabase(ContactJpa expectedContactJpa) {
    expectedContactJpa.setId(idNewContact);
    Iterator<ContactJpa> iterator = contactRepository.findAll().iterator();
    assertThat(iterator).hasNext();
    ContactJpa actualContactJpa = iterator.next();
    assertThat(actualContactJpa).isEqualToComparingFieldByField(expectedContactJpa);
  }

  @Then("the following contact is present in the database, ignoring fields \"{strings}\":")
  public void theFollowingContactIsPresentInTheDatabaseIgnoring(Collection<String> fieldsToIgnore, ContactJpa expectedContactJpa) {
    Iterator<ContactJpa> iterator = contactRepository.findAll().iterator();
    assertThat(iterator).hasNext();
    ContactJpa actualContactJpa = iterator.next();
    assertThat(actualContactJpa).isEqualToIgnoringGivenFields(expectedContactJpa, fieldsToIgnore.toArray(new String[0]));
  }

  @Given("the following user in database")
  public void theFollowingUserInDatabase(ContactJpa contactJpa) {
    ContactJpa newContact = contactRepository.save(contactJpa);
    idNewContact = newContact.getId();
  }

  @Then("there is no contact in the database")
  public void thereIsNoContactInTheDatabase() {
    Iterator<ContactJpa> allContacts = contactRepository.findAll().iterator();
    assertThat(allContacts.hasNext()).isFalse();
  }

  @And("the following event has been published:")
  public void theFollowingEventHasBeenPublished() {

  }

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      environment.start();
      applicationPort = environment.getServicePort("app_1", 8080);
      databasePort = environment.getServicePort("database_1", 3306);
      TestPropertyValues.of(
          String.format("jdbc:mysql://localhost:%d/contacts?createDatabaseIfNotExist=true&serverTimezone=UTC", databasePort),
          String.format("app.url=http://localhost:%d", applicationPort)
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}