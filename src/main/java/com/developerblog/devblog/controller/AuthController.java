package com.developerblog.devblog.controller;

import com.developerblog.devblog.entity.Role;
import com.developerblog.devblog.entity.User;
import com.developerblog.devblog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

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

        // Проверяем пароль на суффикс admin12345
        if (password.endsWith("admin12345")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        try {
            userService.registerUser(user);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("title", "Регистрация");
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            model.addAttribute("content", "auth/register");
            // Сохраняем введенные данные для повторного заполнения формы
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "base";
        }
    }
}