package com.example.demo;

public class Utils {
  public static String buildNewContactUrl(String baseUrl, int id) {
    return baseUrl + "/contact/" + id;
  }
}
