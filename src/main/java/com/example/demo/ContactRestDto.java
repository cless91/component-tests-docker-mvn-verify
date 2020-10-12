package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactRestDto {
  private int id;
  private String name;
  private String email;
  private String phoneNumber;
}
