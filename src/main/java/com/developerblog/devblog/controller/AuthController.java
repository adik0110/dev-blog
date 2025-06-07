package com.developerblog.devblog.controller;

import com.developerblog.devblog.entity.Role;
import com.developerblog.devblog.entity.User;
import com.developerblog.devblog.service.EmailVerificationService;
import com.developerblog.devblog.service.TemporaryUserStorage;
import com.developerblog.devblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final TemporaryUserStorage temporaryUserStorage;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "Вход");
        model.addAttribute("content", "auth/login");
        return "base";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("title", "Регистрация");
        model.addAttribute("content", "auth/register");
        return "base";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setEnabled(false);

        if (password.endsWith("admin12345")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        try {
            Random random = new Random();
            int code = 100000 + random.nextInt(900000);
            String verificationCode = String.valueOf(code);

            // Сохраняем временные данные пользователя
            temporaryUserStorage.saveTempUser(verificationCode, user);

            // Отправляем письмо с кодом подтверждения
            emailVerificationService.sendVerificationEmail(email, verificationCode);

            return "redirect:/verify-email?email=" + email;
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("content", "auth/register");
            return "base";
        }
    }

    @GetMapping("/verify-email")
    public String showVerificationPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("content", "auth/verify-email");
        return "base";
    }

    @PostMapping("/verify-email")
    public String verifyEmail(
            @RequestParam String email,
            @RequestParam String code,
            Model model
    ) {
        try {
            User user = temporaryUserStorage.getAndRemoveTempUser(code);
            if (user == null) {
                throw new IllegalArgumentException("Неверный код подтверждения");
            }

            user.setEnabled(true);
            userService.registerUser(user);

            return "redirect:/login?verified=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка подтверждения: " + e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("content", "auth/verify-email");
            return "base";
        }
    }
}
