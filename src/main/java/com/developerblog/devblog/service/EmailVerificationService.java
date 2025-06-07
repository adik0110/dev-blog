//package com.developerblog.devblog.service;
//
//import okhttp3.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class EmailVerificationService {
//
//    private final OkHttpClient client;
//
//    @Value("${sendgrid.api.key}")
//    private String apiKey;
//
//    @Value("${sendgrid.mail}")
//    private String fromEmail;
//
//    public void sendVerificationEmail(String to, String code) throws IOException {
//
//        String json = """
//        {
//          "personalizations": [
//            {
//              "to": [ { "email": "%s" } ],
//              "subject": "Код подтверждения регистрации"
//            }
//          ],
//          "from": { "email": "%s" },
//          "content": [
//            {
//              "type": "text/plain",
//              "value": "Ваш код подтверждения: %s"
//            }
//          ]
//        }
//        """.formatted(to, fromEmail, code);
//
//        RequestBody body = RequestBody.create(
//                json,
//                MediaType.parse("application/json; charset=utf-8")
//        );
//
//        Request request = new Request.Builder()
//                .url("https://api.sendgrid.com/v3/mail/send")
//                .addHeader("Authorization", "Bearer " + apiKey)
//                .addHeader("Content-Type", "application/json")
//                .post(body)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("SendGrid error: " + response.code() + " - " + response.body().string());
//            }
//        }
//    }
//}
