package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

@SpringBootApplication
public class ServiceA implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ServiceA.class, args);
	}

	@Autowired
	KafkaProperties kafkaProperties;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println();
	}
}
