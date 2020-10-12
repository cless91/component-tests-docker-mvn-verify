package com.example.demo;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;

public class DataTableMappers {
  @DataTableType
  public ContactJpa aContactJpa(DataTable dataTable) {
    return Utils.convertDatatableTransposed(dataTable, ContactJpa.class);
  }

  @DataTableType
  public ContactRestDto aContactRestDto(DataTable dataTable) {
    return Utils.convertDatatableTransposed(dataTable, ContactRestDto.class);
  }
}
