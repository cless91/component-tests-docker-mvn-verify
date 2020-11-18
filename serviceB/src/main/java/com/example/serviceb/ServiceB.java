package com.example.serviceb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceB implements ApplicationRunner {

	@Autowired
	private MyEntityRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ServiceB.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		MyEntity entity = new MyEntity();
		entity.setId(1);
		entity.setName("someValue");
		repository.save(entity);
	}
}
