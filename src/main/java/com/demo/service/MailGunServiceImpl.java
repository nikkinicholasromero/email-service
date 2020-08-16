package com.demo.service;

import com.demo.model.Mail;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!mock")
public class MailGunServiceImpl implements MailGunService {
    @Override
    public boolean send(String url, String api, Mail mail) throws UnirestException {
        return Unirest.post(url)
                .basicAuth("api", api)
                .queryString("from", mail.getFrom())
                .queryString("to", mail.getTo())
                .queryString("subject", mail.getSubject())
                .queryString("text", mail.getSubject())
                .asJson().getStatus() == 200;
    }
}
