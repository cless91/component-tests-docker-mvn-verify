package com.example.demo.cucumber.development;

import com.example.demo.ServiceA;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

@SpringBootTest
@CucumberContextConfiguration
@ContextConfiguration(classes = {ServiceA.class})
@ActiveProfiles("development")
public class CucumberSpringConfigurationDevelopment {
}
