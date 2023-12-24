package com.marekpoliszak.restaurants_review_app.controller;


import com.marekpoliszak.restaurants_review_app.model.Restaurant;
import com.marekpoliszak.restaurants_review_app.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
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
        validateNewRestaurant(newRestaurant);
        restaurantRepository.save(newRestaurant);
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurant(@PathVariable Long id) {
        Optional<Restaurant> existingRestaurantOptional = restaurantRepository.findById(id);
        if(existingRestaurantOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return existingRestaurantOptional.get();
    }

    @GetMapping
    public Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/search")
    public Iterable<Restaurant> getRestaurantsByZipCodeAndAllergies(@RequestParam String zipCode,
                                                                     @RequestParam String allergy) {
        validateZipCode(zipCode);
        Iterable<Restaurant> restaurants = Collections.EMPTY_LIST;
        if(allergy.equalsIgnoreCase("peanut")) {
            restaurants = restaurantRepository.findRestaurantsByZipCodeAndPeanutScoreNotNullOrderedByPeanutScore(zipCode);
        } else if(allergy.equalsIgnoreCase("dairy")) {
            restaurants = restaurantRepository.findRestaurantsByZipCodeAndDairyScoreNotNullOrderByDairyScore(zipCode);
        } else if (allergy.equalsIgnoreCase("egg")) {
            restaurants = restaurantRepository.findRestaurantsByZipCodeAndEggScoreNotNullOrderByEggScore(zipCode);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return restaurants;
    }

    private void validateNewRestaurant(Restaurant newRestaurant) {
        validateZipCode(newRestaurant.getZipCode());

        if(ObjectUtils.isEmpty(newRestaurant.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Restaurant> existingRestaurantOptional = restaurantRepository.findRestaurantsByNameAndZipCode(newRestaurant.getName(), newRestaurant.getZipCode());
        if(existingRestaurantOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    private void validateZipCode(String zipCode) {
        if(!zipCodePattern.matcher(zipCode).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
