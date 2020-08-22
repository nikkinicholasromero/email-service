package com.demo.external.mailgun;

import com.demo.exception.EmailSenderException;
import com.demo.model.Mail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EmailService {
    private static final String API = "api";
    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String SUBJECT = "subject";
    private static final String TEXT = "text";
    private static final String TEMPLATE = "template";
    private static final String H_X_MAILGUN_VARIABLES = "h:X-Mailgun-Variables";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${mailgun.send.host}")
    private String host;

    @Value("${mailgun.send.endpoint}")
    private String endpoint;

    @Value("${mailgun.send.api}")
    private String api;

    public EmailService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public void send(Mail mail) {
        try {
            ResponseEntity<Object> object = restTemplate.postForEntity(getUrl(), getHeaders(mail), Object.class);
            if (HttpStatus.OK != object.getStatusCode()) {
                throw new EmailSenderException();
            }
        } catch (Exception e) {
            throw new EmailSenderException();
        }
    }

    private String getUrl() {
        return UriComponentsBuilder.fromHttpUrl(host + endpoint).toUriString();
    }

    private HttpEntity<Object> getHeaders(Mail mail) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(API, api);
        return new HttpEntity<>(getRequest(mail), headers);
    }

    private MultiValueMap<String, String> getRequest(Mail mail) {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add(FROM, mail.getFrom());
            map.add(TO, mail.getTo());
            map.add(SUBJECT, mail.getSubject());
            map.add(TEXT, mail.getBody());
            map.add(TEMPLATE, mail.getTemplate());
            map.add(H_X_MAILGUN_VARIABLES, objectMapper.writeValueAsString(mail.getTemplateVariables()));
            return map;
        } catch (JsonProcessingException e) {
            throw new EmailSenderException();
        }
    }
}
