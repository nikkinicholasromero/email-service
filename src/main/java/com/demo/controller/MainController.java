package com.demo.controller;

import com.demo.model.Mail;
import com.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MainController {
    @Autowired
    private EmailService emailService;

    @PutMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void main(@RequestBody Mail mail) {
        emailService.sendEmail(mail);
    }
}
