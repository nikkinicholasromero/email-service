package com.demo.servicer;

import com.demo.model.Mail;
import com.demo.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmailServiceTest {
    @InjectMocks
    private EmailService target;

    @Mock
    private JavaMailSender javaMailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void send() {
        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        target.send(mail);

        verify(javaMailSender, times(1)).send(simpleMailMessageArgumentCaptor.capture());

        SimpleMailMessage message = simpleMailMessageArgumentCaptor.getValue();
        assertThat(message).isNotNull();
        assertThat(message.getFrom()).isEqualTo("from@email.com");
        assertThat(message.getTo()).isEqualTo(new String[]{"to@email.com"});
        assertThat(message.getSubject()).isEqualTo("Test Subject");
        assertThat(message.getText()).isEqualTo("Test body");
    }
}
