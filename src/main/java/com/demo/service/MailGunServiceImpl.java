package com.demo.service;

import com.demo.exception.EmailSenderException;
import com.demo.model.Mail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!mock")
public class MailGunServiceImpl implements MailGunService {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean sendTextBody(String url, String api, Mail mail) {
        try {
            return Unirest.post(url)
                    .basicAuth("api", api)
                    .queryString("from", mail.getFrom())
                    .queryString("to", mail.getTo())
                    .queryString("subject", mail.getSubject())
                    .queryString("text", mail.getBody())
                    .asJson().getStatus() == 200;
        } catch (UnirestException e) {
            throw new EmailSenderException();
        }
    }

    @Override
    public boolean sendTemplate(String url, String api, Mail mail) {
        try {
            return Unirest.post(url)
                    .basicAuth("api", api)
                    .field("from", mail.getFrom())
                    .field("to", mail.getTo())
                    .field("subject", mail.getSubject())
                    .field("template", mail.getTemplate())
                    .field("h:X-Mailgun-Variables", objectMapper.writeValueAsString(mail.getTemplateVariables()))
                    .asJson().getStatus() == 200;
        } catch (UnirestException | JsonProcessingException e) {
            throw new EmailSenderException();
        }
    }
}
