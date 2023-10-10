package com.marekpoliszak.restaurants_review_app.controller;

import com.marekpoliszak.restaurants_review_app.model.User;
import com.marekpoliszak.restaurants_review_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public void addUser(@RequestBody User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        Optional<User> optionalUser = userRepository.findUserByDisplayName(user.displayName());
    }
}
