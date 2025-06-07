package com.developerblog.devblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final RestTemplate restTemplate;

    public void sendVerificationEmail(String email, String code) {
        String fastApiUrl = "http://localhost:8001/send-verification-email";

        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("code", code);

        ResponseEntity<String> response = restTemplate.postForEntity(
                fastApiUrl,
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Не удалось отправить письмо подтверждения");
        }
    }
}

