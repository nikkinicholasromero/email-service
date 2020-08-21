package com.demo;

import com.demo.mock.MockRestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ApplicationConfig {
    @Bean
    @Profile("mock")
    public RestTemplateBuilder restTemplateBuilder() {
        return new MockRestTemplateBuilder();
    }
}
