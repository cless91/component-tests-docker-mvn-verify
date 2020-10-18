package com.example.serviceb;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "entities")
public interface MyEntityRepository extends PagingAndSortingRepository<MyEntity, Integer> {
}
