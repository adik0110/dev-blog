package com.developerblog.devblog.service;

import com.developerblog.devblog.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TemporaryUserStorage {
    private final Map<String, User> tempUsers = new ConcurrentHashMap<>();

    public void saveTempUser(String code, User user) {
        tempUsers.put(code, user);
    }

    public User getAndRemoveTempUser(String code) {
        return tempUsers.remove(code);
    }
}