package com.marekpoliszak.restaurants_review_app.controller;

import com.marekpoliszak.restaurants_review_app.model.User;
import com.marekpoliszak.restaurants_review_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
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
        validateUser(user);
        userRepository.save(user);
    }

    @GetMapping("/{displayName}")
    public User getUser (@PathVariable String displayName) {
        validateDisplayName(displayName);
        Optional<User> optionalUser = userRepository.findUserByDisplayName(displayName);
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        return user;
    }

    @PutMapping("/{displayName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserInfo(@PathVariable String displayName, @RequestBody User updatedUser) {
        validateDisplayName(displayName);
        Optional<User> existingUserOptional = userRepository.findUserByDisplayName(displayName);
        if(existingUserOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User existingUser = existingUserOptional.get();
        copyUserInfo(updatedUser, existingUser);
        userRepository.save(existingUser);
    }

    private void copyUserInfo(User source, User destination) {
        if (ObjectUtils.isEmpty(source.getDisplayName())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!ObjectUtils.isEmpty(source.getCity())) {
            destination.setCity(source.getCity());
        }

        if (!ObjectUtils.isEmpty(source.getState())) {
            destination.setState(source.getState());
        }

        if (!ObjectUtils.isEmpty(source.getZipCode())) {
            destination.setZipCode(source.getZipCode());
        }

        if (!ObjectUtils.isEmpty(source.getPeanutWatch())) {
            destination.setPeanutWatch(source.getPeanutWatch());
        }

        if (!ObjectUtils.isEmpty(source.getDairyWatch())) {
            destination.setDairyWatch(source.getDairyWatch());
        }

        if (!ObjectUtils.isEmpty(source.getEggWatch())) {
            destination.setEggWatch(source.getEggWatch());
        }
    }

    private void validateDisplayName(String displayName) {
        if(ObjectUtils.isEmpty(displayName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void validateUser(User user) {
        validateDisplayName(user.getDisplayName());

        Optional<User> existingUser = userRepository.findUserByDisplayName(user.getDisplayName());
        if(existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

}
