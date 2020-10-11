package com.example.demo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Contact {
  @Id
  @GeneratedValue
  private int id;

  private String name;
  private String email;
  private String phoneNumber;
}
