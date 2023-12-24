package com.marekpoliszak.restaurants_review_app.controller;

import com.marekpoliszak.restaurants_review_app.model.Restaurant;
import com.marekpoliszak.restaurants_review_app.model.Review;
import com.marekpoliszak.restaurants_review_app.model.ReviewStatus;
import com.marekpoliszak.restaurants_review_app.model.User;
import com.marekpoliszak.restaurants_review_app.repository.RestaurantRepository;
import com.marekpoliszak.restaurants_review_app.repository.ReviewRepository;
import com.marekpoliszak.restaurants_review_app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(RestaurantRepository restaurantRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addUserReview(@RequestBody Review review) {
        validateUserReview(review);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(review.getRestaurantId());
        if(optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        review.setStatus(ReviewStatus.PENDING);
        reviewRepository.save(review);
    }

    private void validateUserReview(Review review) {
        if(ObjectUtils.isEmpty(review.getSubmittedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(ObjectUtils.isEmpty(review.getRestaurantId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(ObjectUtils.isEmpty(review.getDairyScore())
                && ObjectUtils.isEmpty(review.getEggScore())
                && ObjectUtils.isEmpty(review.getPeanutScore())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<User> optionalUser = userRepository.findUserByDisplayName(review.getSubmittedBy());
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
