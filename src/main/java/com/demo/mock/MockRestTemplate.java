package com.demo.mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

public class MockRestTemplate extends RestTemplate {
    @Override
    public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
