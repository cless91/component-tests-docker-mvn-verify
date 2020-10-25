package com.example.demo.cucumber.steps;


import com.example.demo.ContactEvent;
import com.example.demo.Contact;
import com.example.demo.ContactRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
public class ServiceACucumberSteps {
  @Autowired
  private ContactRepository contactRepository;
  @Autowired
  private EventRepository eventRepository;
  @Value("${app.base.url}")
  private String appBaseUrl;
  @Value("${serviceb.base.url}")
  private String serviceBBaseUrl;
  private int timeoutMillis = 7000;
  private final RestTemplate restTemplate = new RestTemplate();
  private ResponseEntity<Void> actualCreateContactResponseEntity;
  private ResponseEntity<Void> actualUpdateContactResponseEntity;
  int idNewContact;

  @Autowired
  Environment environment;

  @Given("the application is up and ready")
  public void theApplicationIsUpAndRunning() {
    await().atMost(Duration.ofMillis(timeoutMillis))
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
    Iterable<Contact> allContacts = contactRepository.findAll();
    assertThat(allContacts).isEmpty();
  }

  @When("the following \"CREATE CONTACT\" REST request is sent:")
  public void theFollowingCreateContactRESTRequestIsSent(Contact contact) {
    String url = appBaseUrl + "/contacts";
    log.info("sending create contact request to url {}", url);
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Correlation-ID", TestContext.getCorrelationId());
    HttpEntity<Contact> requestEntity = new HttpEntity<>(contact, headers);
//    actualCreateContactResponseEntity = restTemplate.postForEntity(url, contactRestDto, Void.TYPE);
    actualCreateContactResponseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.TYPE);
  }

  @When("the following \"UPDATE CONTACT\" REST request is sent:")
  public void theFollowingUpdateContactRESTRequestIsSent(Contact contact) {
    String url = appBaseUrl + "/contacts/" + idNewContact;
    log.info("sending update contact request to url {}", url);
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Correlation-ID", TestContext.getCorrelationId());
    HttpEntity<Contact> requestEntity = new HttpEntity<>(contact, headers);
    actualUpdateContactResponseEntity = restTemplate.exchange(url,
        HttpMethod.PUT,
        requestEntity,
        Void.TYPE);
  }

  @When("a \"DELETE CONTACT\" REST request is sent")
  public void aDeleteContactRESTRequestIsSent() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Correlation-ID", TestContext.getCorrelationId());
    HttpEntity<Contact> requestEntity = new HttpEntity<>(headers);
    actualUpdateContactResponseEntity = restTemplate.exchange(appBaseUrl + "/contacts/" + idNewContact,
        HttpMethod.DELETE,
        requestEntity,
        Void.TYPE);
  }

  @Then("the following contact is present in the database:")
  public void theFollowingContactIsPresentInTheDatabase(Contact expectedContact) {
    expectedContact.setId(idNewContact);
    Iterator<Contact> iterator = contactRepository.findAll().iterator();
    assertThat(iterator).hasNext();
    Contact actualContact = iterator.next();
    assertThat(actualContact).isEqualToComparingFieldByField(expectedContact);
  }

  @Then("the following contact is present in the database, ignoring fields \"{strings}\":")
  public void theFollowingContactIsPresentInTheDatabaseIgnoring(Collection<String> fieldsToIgnore, Contact expectedContact) {
    Iterator<Contact> iterator = contactRepository.findAll().iterator();
    assertThat(iterator).hasNext();
    Contact actualContact = iterator.next();
    assertThat(actualContact).isEqualToIgnoringGivenFields(expectedContact, fieldsToIgnore.toArray(new String[0]));
  }

  @Given("the following user in database")
  public void theFollowingUserInDatabase(Contact contact) {
    Contact newContact = contactRepository.save(contact);
    idNewContact = newContact.getId();
  }

  @Then("there is no contact in the database")
  public void thereIsNoContactInTheDatabase() {
    Iterator<Contact> allContacts = contactRepository.findAll().iterator();
    assertThat(allContacts.hasNext()).isFalse();
  }

  @And("the following event has been published:")
  public void theFollowingEventHasBeenPublished(ContactEvent expectedContactEvent) {
    ContactEvent contactEvent = await().atMost(Duration.ofSeconds(5))
        .until(() -> eventRepository.getByCorrelationId(TestContext.getCorrelationId()),
            Optional::isPresent).get();

  }

  @Given("service-b replies with data {string}")
  public void serviceBRepliesWithData(String data) {
    Map<String,Object> request = new HashMap<>();
    request.put("name", data);
    log.info("posting test data to service-b on url: {}",serviceBBaseUrl);
    restTemplate.postForEntity(serviceBBaseUrl+"/given",request,Void.TYPE);
  }
}