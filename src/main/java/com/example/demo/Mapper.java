package com.example.demo;

public class Mapper {
  public static ContactJpa dto2Jpa(ContactRestDto dto) {
    ContactJpa jpa = new ContactJpa();
    jpa.setId(dto.getId());
    jpa.setName(dto.getName());
    jpa.setEmail(dto.getEmail());
    jpa.setPhoneNumber(dto.getPhoneNumber());
    return jpa;
  }

  public static ContactRestDto jpa2Dtp(ContactJpa jpa) {
    ContactRestDto dto = new ContactRestDto();
    dto.setId(jpa.getId());
    dto.setName(jpa.getName());
    dto.setEmail(jpa.getEmail());
    dto.setPhoneNumber(jpa.getPhoneNumber());
    return dto;
  }
}
