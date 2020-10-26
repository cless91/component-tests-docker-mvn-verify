package com.example.demo.cucumber.development;

import com.example.demo.ServiceA;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@CucumberContextConfiguration
@ContextConfiguration(classes = {ServiceA.class})
@ActiveProfiles("development")
public class CucumberSpringConfigurationDevelopment {
}
