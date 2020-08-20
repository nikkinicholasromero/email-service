package com.demo.service;

import com.demo.model.Mail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface MailGunService {
    boolean sendTextBody(String url, String api, Mail mail);
    boolean sendTemplate(String url, String api, Mail mail);
}
