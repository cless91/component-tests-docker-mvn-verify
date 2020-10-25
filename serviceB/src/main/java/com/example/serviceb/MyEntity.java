package com.example.serviceb;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class MyEntity {
  @Id
  @GeneratedValue
  private int id;
  private String name;
}
