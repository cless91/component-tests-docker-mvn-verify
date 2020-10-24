package com.example.demo.cucumber.steps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Data
public class ApplicationStatus {
  private String status;
  private Components components;

  public String getStatus() {
    return status;
  }

  public String getDatabaseStatus() {
    return Optional.ofNullable(components)
        .map(components -> components.db)
        .map(db -> db.status)
        .orElse(null);
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Components {
    private Db db;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Db {
      private String status;
    }
  }
}
