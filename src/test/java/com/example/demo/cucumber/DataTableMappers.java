package com.example.demo.cucumber;

import com.example.demo.ContactJpa;
import com.example.demo.ContactRestDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class DataTableMappers {
  @DataTableType
  public ContactJpa aContactJpa(DataTable dataTable) {
    return TestUtils.convertDatatableTransposed(dataTable, ContactJpa.class);
  }

  @DataTableType
  public ContactRestDto aContactRestDto(DataTable dataTable) {
    return TestUtils.convertDatatableTransposed(dataTable, ContactRestDto.class);
  }

  @ParameterType(
      value = ".+",
      name = "strings")
  public Collection<String> strings(String string) {
    return Arrays.stream(string.trim().split("\\s*,\\s*")).collect(Collectors.toCollection(ArrayList::new));
  }
}
