package com.demo.controller;

import com.demo.model.Mail;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mock")
public class EmailControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void send_whenSendingIsSuccessful_thenReturn201StatusCode() throws Exception {
        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("to@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        this.mockMvc.perform(put("/mail")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mail)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void send_whenSendingFailed_thenReturn503StatusCode() throws Exception {
        Mail mail = new Mail();
        mail.setFrom("from@email.com");
        mail.setTo("invalid@email.com");
        mail.setSubject("Test Subject");
        mail.setBody("Test body");

        this.mockMvc.perform(put("/mail")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mail)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
