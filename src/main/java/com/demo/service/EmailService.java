package com.demo.service;

import com.demo.exception.EmailSenderException;
import com.demo.model.Mail;
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
        boolean successful;

        if (mail.getTemplate() != null) {
            successful = mailGunService.sendTemplate(host + endpoint, api, mail);
        } else {
            successful = mailGunService.sendTextBody(host + endpoint, api, mail);
        }

        if (!successful) {
            throw new EmailSenderException();
        }
    }
}
