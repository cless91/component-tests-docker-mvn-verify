package com.example.demo.cucumber.steps;

import com.example.demo.Contact;
import com.example.demo.ContactEvent;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class DataTableMappers {
  @ParameterType(
      value = ".+",
      name = "strings")
  public Collection<String> strings(String string) {
    return Arrays.stream(string.trim().split("\\s*,\\s*")).collect(Collectors.toCollection(ArrayList::new));
  }

  @DataTableType
  public Contact aContactJpa(DataTable dataTable) {
    return TestUtils.convertDatatableTransposed(dataTable, Contact.class);
  }

  @DataTableType
  public ContactEvent aContactEvent(DataTable dataTable) {
    ContactEvent contactEvent = TestUtils.convertDatatableTransposed(dataTable, ContactEvent.class);
    contactEvent.setCorrelationId(TestContext.getCorrelationId());
    return contactEvent;
  }
}
