package com.example.demo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "contacts")
public interface ContactRepository extends PagingAndSortingRepository<Contact, Integer> {
}
