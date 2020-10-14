package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
public class ContactRestController {
  /**
   * Todo un test avec une bdd embarquée type H2 pour vérifier que le header `Location` est bien positionné après un insert
   */

  @Autowired
  ContactRepository contactRepository;

  @Autowired
  ContactCrudService contactCrudService;

  @RequestMapping(path = "/contact", method = RequestMethod.POST)
  public ResponseEntity<Void> create(@RequestBody ContactRestDto request, @Header("X-Correlation-ID") String inputCorrelationID) {
    String correlationId = inputCorrelationID != null ? inputCorrelationID : UUID.randomUUID().toString();
    final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    ContactRestDto savedContact = contactCrudService.create(request, correlationId, baseUrl);
    String locationUrl = buildNewContactUrl(savedContact, baseUrl);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("Location", locationUrl)
        .header("X-Correlation-ID", correlationId)
        .build();
  }

  private String buildNewContactUrl(ContactRestDto savedContact, String baseUrl) {
    return baseUrl + "/contact/" + savedContact.getId();
  }

  @RequestMapping(path = "/contact/{id}", method = RequestMethod.GET)
  public ResponseEntity<ContactRestDto> get(@PathVariable("id") int id) {
    return contactRepository.findById(id)
        .map(contactJpa -> {
          ContactRestDto dto = new ContactRestDto();
          dto.setId(contactJpa.getId());
          dto.setName(contactJpa.getName());
          dto.setEmail(contactJpa.getEmail());
          dto.setPhoneNumber(contactJpa.getPhoneNumber());
          return dto;
        })
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping(path = "/contact/{id}", method = RequestMethod.PUT)
  public ResponseEntity<Void> update(@PathVariable("id") int id, @RequestBody ContactRestDto dto) {
    ContactJpa contactJpa = Mapper.dto2Jpa(dto);
    contactJpa.setId(id);
    contactRepository.save(contactJpa);
    return ResponseEntity.ok().build();
  }

  @RequestMapping(path = "/contact/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(@PathVariable("id") int id) {
    contactRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
