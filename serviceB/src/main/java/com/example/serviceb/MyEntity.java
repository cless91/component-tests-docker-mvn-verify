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
//  @GeneratedValue(generator = "UUID")
//  @GenericGenerator(
//      name = "UUID",
//      strategy = "org.hibernate.id.UUIDGenerator"
//  )
//  @Column(name = "id", updatable = false, nullable = false)
//  private UUID id;
  private int id;
  private String name;
}
