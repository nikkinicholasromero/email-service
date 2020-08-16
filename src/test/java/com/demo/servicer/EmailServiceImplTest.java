package com.demo.servicer;

import com.demo.exception.EmailSenderException;
import com.demo.model.Mail;
import com.demo.service.EmailService;
import com.demo.service.MailGunService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailServiceImplTest {
    private static final String host = "https:/somehost.com";
    private static final String endpoint = "/someEndpoint";
    private static final String api = "some-api-key";

    @InjectMocks
    private EmailService target;

    @Mock
    private MailGunService mailGunService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(target, "host", host);
        ReflectionTestUtils.setField(target, "endpoint", endpoint);
        ReflectionTestUtils.setField(target, "api", api);
    }

    @Test
    public void send_whenMailGunServiceReturnsTrue_thenDoNotThrowAnyExceptions() throws UnirestException {
        when(mailGunService.send(anyString(), anyString(), any(Mail.class))).thenReturn(true);

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        target.send(mail);

        verify(mailGunService).send(host + endpoint, api, mail);
    }

    @Test
    public void send_whenMailGunServiceReturnsFalse_thenThrowEmailSenderException() throws UnirestException {
        when(mailGunService.send(anyString(), anyString(), any(Mail.class))).thenReturn(false);

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        assertThrows(EmailSenderException.class, () -> target.send(mail));

        verify(mailGunService).send(host + endpoint, api, mail);
    }

    @Test
    public void send_whenMailGunServiceThrowsUnirestException_thenThrowEmailSenderException() throws UnirestException {
        when(mailGunService.send(anyString(), anyString(), any(Mail.class))).thenThrow(UnirestException.class);

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        assertThrows(EmailSenderException.class, () -> target.send(mail));

        verify(mailGunService).send(host + endpoint, api, mail);
    }
}
