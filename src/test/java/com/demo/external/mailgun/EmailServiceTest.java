package com.demo.external.mailgun;

import com.demo.exception.EmailSenderException;
import com.demo.model.Mail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RunWith(SpringRunner.class)
@RestClientTest(EmailService.class)
public class EmailServiceTest {
    @Autowired
    private EmailService target;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void send_whenRequestIsSuccessful() {
        ReflectionTestUtils.setField(target, "api", "12345abc");

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        server.expect(requestTo("https://api.mailgun.net/v3/mg.nikkinicholasromero.com/messages"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Basic YXBpOjEyMzQ1YWJj"))
                .andExpect(content().string("from=from%40email.com&to=to%40email.com&subject=Test+Subject&text=Test+body&template&h%3AX-Mailgun-Variables=null"))
                .andRespond(withSuccess());

        target.send(mail);
    }

    @Test
    public void send_whenStatusIsNot200_thenThrowEmailSenderException() {
        ReflectionTestUtils.setField(target, "api", "12345abc");

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        server.expect(requestTo("https://api.mailgun.net/v3/mg.nikkinicholasromero.com/messages"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Basic YXBpOjEyMzQ1YWJj"))
                .andExpect(content().string("from=from%40email.com&to=to%40email.com&subject=Test+Subject&text=Test+body&template&h%3AX-Mailgun-Variables=null"))
                .andRespond(withNoContent());

        assertThrows(EmailSenderException.class, () -> target.send(mail));
    }

    @Test
    public void send_whenStatusIs500_thenThrowEmailSenderException() {
        ReflectionTestUtils.setField(target, "api", "12345abc");

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        server.expect(requestTo("https://api.mailgun.net/v3/mg.nikkinicholasromero.com/messages"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Basic YXBpOjEyMzQ1YWJj"))
                .andExpect(content().string("from=from%40email.com&to=to%40email.com&subject=Test+Subject&text=Test+body&template&h%3AX-Mailgun-Variables=null"))
                .andRespond(withServerError());

        assertThrows(EmailSenderException.class, () -> target.send(mail));
    }
}
