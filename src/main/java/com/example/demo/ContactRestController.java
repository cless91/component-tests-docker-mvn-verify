package com.example.demo;

import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ContactRestController {
  /**
   * Todo un test avec une bdd embarquée type H2 pour vérifier que le header `Location` est bien positionné après un insert
   */

  @Autowired
  ContactRepository contactRepository;

  @RequestMapping(path = "/contact", method = RequestMethod.POST)
  public ResponseEntity<Void> create(@RequestBody ContactRestDto request) {
    ContactJpa contactJpa = new ContactJpa();
    contactJpa.setName(request.getName());
    contactJpa.setEmail(request.getEmail());
    contactJpa.setPhoneNumber(request.getPhoneNumber());

    ContactJpa savedContact = contactRepository.save(contactJpa);
    final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("Location", baseUrl + "/contact/" + savedContact.getId())
        .build();
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
}
