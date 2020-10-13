package com.example.demo.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"html:target/cucumber-html-report",
        "json:target/cucumber.json", "junit:target/cucumber-results.xml",
        "pretty:target/cucumber-pretty.txt", "usage:target/cucumber-usage.json"},
        glue = "com.example.demo.cucumber",
        features = "src/test/resources/features")
public class CucumberRunnerIT {
}
