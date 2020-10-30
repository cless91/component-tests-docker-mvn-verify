package com.example.demo.cucumber;

import com.example.demo.ServiceBGateway;
import com.example.demo.cucumber.steps.ServiceBConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.doReturn;

@Slf4j
@Component
@Profile("unittest")
public class ServiceBConfigurerMock implements ServiceBConfigurer {

  private ServiceBGateway serviceBGateway;

  public ServiceBConfigurerMock(ServiceBGateway serviceBGateway) {
    this.serviceBGateway = serviceBGateway;
  }

  @Override
  public void configureResponse(String data) {
    doReturn(data).when(serviceBGateway).getServiceBValue();
  }
}
