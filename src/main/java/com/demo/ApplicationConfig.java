package com.demo;

import com.demo.mock.MockJavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class ApplicationConfig {
    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private int port;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.transport.protocol}")
    private String protocol;

    @Value("${email.smtp.auth}")
    private String smpthAuth;

    @Value("${email.smtp.starttls.enable}")
    private String smptpStarttlsEnable;

    @Value("${email.debug}")
    private String debug;

    @Bean
    @Primary
    @Profile("!mock")
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", smpthAuth);
        props.put("mail.smtp.starttls.enable", smptpStarttlsEnable);
        props.put("mail.debug", debug);

        return mailSender;
    }

    @Bean
    @Profile("mock")
    public JavaMailSender mockJavaMailSender() {
        return new MockJavaMailSender();
    }
}
