package com.demo.service;

import com.demo.model.Mail;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface MailGunService {
    boolean send(String url, String api, Mail mail) throws UnirestException;
}
