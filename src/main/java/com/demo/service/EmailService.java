package com.demo.service;

import com.demo.exception.EmailSenderException;
import com.demo.model.Mail;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${mailgun.send.host}")
    private String host;

    @Value("${mailgun.send.endpoint}")
    private String endpoint;

    @Value("${mailgun.send.api}")
    private String api;

    @Autowired
    private MailGunService mailGunService;

    public void send(Mail mail) {
        try {
            boolean successful = mailGunService.send(host + endpoint, api, mail);
            if (!successful) {
                throw new UnirestException("");
            }
        } catch (UnirestException e) {
            throw new EmailSenderException();
        }
    }
}
