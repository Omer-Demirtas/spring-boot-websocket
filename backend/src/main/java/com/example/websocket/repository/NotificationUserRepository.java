package com.example.websocket.repository;

import com.example.websocket.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationUserRepository {

    private final List<User> users = new ArrayList<>();

    public void add(Long id) { users.add(new User(id)); }

    public void remove(Long id) {users.removeIf(user -> user.getId().equals(id)); }
}
