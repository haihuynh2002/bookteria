package com.devteria.profile.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devteria.profile.dto.ApiResponse;
import com.devteria.profile.dto.request.SendEmailRequest;
import com.devteria.profile.dto.response.EmailResponse;
import com.devteria.profile.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailController {
    EmailService emailService;

    @PostMapping("/email/send")
    public ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest request) {
        ApiResponse<EmailResponse> response = ApiResponse.<EmailResponse>builder()
                .result(emailService.sendEmail(request))
                .build();
        return response;
    }

    @KafkaListener(topics = "onboard-successful")
    public void listener(String message) {
        log.info("received message: " + message);
    }
}
