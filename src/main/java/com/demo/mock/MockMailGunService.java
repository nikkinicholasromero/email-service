package com.demo.mock;

import com.demo.model.Mail;
import com.demo.service.MailGunService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockMailGunService implements MailGunService {
    @Override
    public boolean sendTextBody(String url, String api, Mail mail) {
        return "to@email.com".equals(mail.getTo());
    }

    @Override
    public boolean sendTemplate(String url, String api, Mail mail) {
        return "to@email.com".equals(mail.getTo());
    }
}
