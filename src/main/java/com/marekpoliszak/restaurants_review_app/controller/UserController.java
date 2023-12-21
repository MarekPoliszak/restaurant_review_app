package com.marekpoliszak.restaurants_review_app.controller;

import com.marekpoliszak.restaurants_review_app.model.User;
import com.marekpoliszak.restaurants_review_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody User user) {
        userRepository.save(user);
    }



    @PutMapping("/{displayName}")
    public void updateUserInfo(@RequestParam String displayName, @RequestBody User updatedUser) {
        Optional<User> userToUpdateOptional = userRepository.findUserByDisplayName(displayName);
        if(userToUpdateOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User info can't be updated, because user is not found");
        }
        User userToUpdate = userToUpdateOptional.get();

        userRepository.save(userToUpdate);
    }

}
