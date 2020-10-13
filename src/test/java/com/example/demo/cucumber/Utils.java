package com.example.demo.cucumber;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import io.cucumber.datatable.DataTable;
import io.vavr.control.Try;

import java.util.Map;

public class Utils {
  private static ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .registerModule(new Jdk8Module())
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);

  public static <T> T convertDatatable(DataTable dataTable, Class<T> clazz) {
    Map<String, String> map = dataTable.asMaps().get(0);
    return Try.of(() -> objectMapper.readValue(JsonUnflattener.unflatten(objectMapper.writeValueAsString(map)), clazz)).get();
  }

  public static <T> T convertDatatableTransposed(DataTable dataTable, Class<T> clazz) {
    Map<String, String> map = dataTable.transpose().asMaps().get(0);
    return Try.of(() -> objectMapper.readValue(JsonUnflattener.unflatten(objectMapper.writeValueAsString(map)), clazz)).get();
  }
}
