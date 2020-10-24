package com.example.demo.cucumber.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"html:target/cucumber-html-report",
    "json:target/cucumber.json", "junit:target/cucumber-results.xml",
    "pretty:target/cucumber-pretty.txt", "usage:target/cucumber-usage.json"},
    glue = {"com.example.demo.cucumber.steps", "com.example.demo.cucumber.component"},
    features = "src/test/resources/features")
public class CucumberRunnerIT {
}
