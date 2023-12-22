package com.marekpoliszak.restaurants_review_app.controller;


import com.marekpoliszak.restaurants_review_app.model.Restaurant;
import com.marekpoliszak.restaurants_review_app.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Pattern;

@RequestMapping("/restaurants")
@RestController
public class RestaurantController {
    public final RestaurantRepository restaurantRepository;
    public final Pattern zipCodePattern = Pattern.compile("^\\d{2}-\\d{3}$\n");

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private void addRestaurant(@RequestBody Restaurant newRestaurant) {
        //validateRestaurant(newRestaurant);
        restaurantRepository.save(newRestaurant);
    }

    @GetMapping("/{id}")
    private Restaurant getRestaurant(@PathVariable Long id) {
        Optional<Restaurant> existingRestaurantOptional = restaurantRepository.findById(id);
        if(existingRestaurantOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return existingRestaurantOptional.get();
    }

    @GetMapping
    private Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    private Iterable<Restaurant> getRestaurantsByZipCodeAndAllergies(String zipCode, String allergy) {

    }

}
