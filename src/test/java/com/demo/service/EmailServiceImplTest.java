package com.demo.service;

import com.demo.exception.EmailSenderException;
import com.demo.model.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    public void send_whenTemplateIsNotNullAndMailGunServiceReturnsTrue_thenDoNotThrowAnyExceptions() {
        when(mailGunService.sendTextBody(anyString(), anyString(), any(Mail.class))).thenReturn(true);
        when(mailGunService.sendTemplate(anyString(), anyString(), any(Mail.class))).thenReturn(true);

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setTemplate("activation_template");
        mail.setTemplateVariables("{\"accountActivationLink\": \"https://github.com/nikkinicholasromero\"}");

        target.send(mail);

        verify(mailGunService).sendTemplate(host + endpoint, api, mail);
        verify(mailGunService, never()).sendTextBody(anyString(), anyString(), any(Mail.class));
    }

    @Test
    public void send_whenTemplateIsNotNullAndMailGunServiceReturnsFalse_thenThrowEmailSenderException() {
        when(mailGunService.sendTextBody(anyString(), anyString(), any(Mail.class))).thenReturn(false);
        when(mailGunService.sendTemplate(anyString(), anyString(), any(Mail.class))).thenReturn(false);

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setTemplate("activation_template");
        mail.setTemplateVariables("{\"accountActivationLink\": \"https://github.com/nikkinicholasromero\"}");

        assertThrows(EmailSenderException.class, () -> target.send(mail));

        verify(mailGunService).sendTemplate(host + endpoint, api, mail);
        verify(mailGunService, never()).sendTextBody(anyString(), anyString(), any(Mail.class));
    }

    @Test
    public void send_whenTemplateIsNullAndMailGunServiceReturnsTrue_thenDoNotThrowAnyExceptions() {
        when(mailGunService.sendTextBody(anyString(), anyString(), any(Mail.class))).thenReturn(true);
        when(mailGunService.sendTemplate(anyString(), anyString(), any(Mail.class))).thenReturn(true);

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        target.send(mail);

        verify(mailGunService).sendTextBody(host + endpoint, api, mail);
        verify(mailGunService, never()).sendTemplate(anyString(), anyString(), any(Mail.class));
    }

    @Test
    public void send_whenTemplateIsNullAndMailGunServiceReturnsFalse_thenThrowEmailSenderException() {
        when(mailGunService.sendTextBody(anyString(), anyString(), any(Mail.class))).thenReturn(false);
        when(mailGunService.sendTemplate(anyString(), anyString(), any(Mail.class))).thenReturn(false);

        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        assertThrows(EmailSenderException.class, () -> target.send(mail));

        verify(mailGunService).sendTextBody(host + endpoint, api, mail);
        verify(mailGunService, never()).sendTemplate(anyString(), anyString(), any(Mail.class));
    }
}
