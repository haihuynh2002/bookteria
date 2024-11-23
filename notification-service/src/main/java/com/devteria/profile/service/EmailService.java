package com.devteria.profile.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devteria.profile.dto.request.EmailRequest;
import com.devteria.profile.dto.request.SendEmailRequest;
import com.devteria.profile.dto.request.Sender;
import com.devteria.profile.dto.response.EmailResponse;
import com.devteria.profile.repository.httpclient.EmailClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @NonFinal
    String apiKey = "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";

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
