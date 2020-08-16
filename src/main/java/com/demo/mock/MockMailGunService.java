package com.demo.mock;

import com.demo.model.Mail;
import com.demo.service.MailGunService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockMailGunService implements MailGunService {
    @Override
    public boolean send(String url, String api, Mail mail) {
        return "to@email.com".equals(mail.getTo());
    }
}
