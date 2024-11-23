package com.devteria.notification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.devteria.notification.dto.request.EmailRequest;
import com.devteria.notification.dto.request.SendEmailRequest;
import com.devteria.notification.dto.request.Sender;
import com.devteria.notification.dto.response.EmailResponse;
import com.devteria.notification.repository.httpclient.EmailClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    EmailClient emailClient;

    @NonFinal
    @Value("${notification.email.brevo-api-key}")
    String apiKey;

    public EmailResponse sendEmail(SendEmailRequest request) {
        var sender = Sender.builder()
                .name("Devteria")
                .email("info.haihuynh@gmail.com")
                .build();

        var emailRequest = EmailRequest.builder()
                .sender(sender)
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        return emailClient.sendEmail(apiKey, emailRequest);
    }
}
