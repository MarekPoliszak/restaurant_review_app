package com.marekpoliszak.restaurants_review_app.controller;

import com.marekpoliszak.restaurants_review_app.model.AdminReviewAction;
import com.marekpoliszak.restaurants_review_app.model.Restaurant;
import com.marekpoliszak.restaurants_review_app.model.Review;
import com.marekpoliszak.restaurants_review_app.model.ReviewStatus;
import com.marekpoliszak.restaurants_review_app.repository.RestaurantRepository;
import com.marekpoliszak.restaurants_review_app.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public AdminController(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/reviews")
    public List<Review> getReviewByStatus(@RequestParam String review_status) {
        ReviewStatus reviewStatus = ReviewStatus.PENDING;

        try {
            reviewStatus = ReviewStatus.valueOf(review_status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return reviewRepository.findReviewsByStatus(reviewStatus);
    }
    @PutMapping("reviews/{reviewId}")
    public void preformReviewAction(@RequestParam Long reviewId, @RequestBody AdminReviewAction reviewAction) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if(optionalReview.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Review review = optionalReview.get();

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(review.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Restaurant restaurant = optionalRestaurant.get();

        if(reviewAction.getAccept()) {
            review.setStatus(ReviewStatus.ACCEPTED);
        } else {
            review.setStatus(ReviewStatus.REJECTED);
        }

        reviewRepository.save(review);

        updateRestaurantReviewScores(restaurant);
    }

    private void updateRestaurantReviewScores(Restaurant restaurant) {
        List<Review> reviews = reviewRepository.findReviewsByStatus(ReviewStatus.ACCEPTED);
        if(reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int peanutSum = 0;
        int dairySum = 0;
        int eggSum = 0;
        int peanutCount = 0;
        int dairyCount = 0;
        int eggCount = 0;

        for (Review review: reviews) {
            if(!ObjectUtils.isEmpty(review.getPeanutScore())) {
            peanutSum += review.getPeanutScore();
            peanutCount++;
            }
            if(!ObjectUtils.isEmpty(review.getDairyScore())) {
            dairySum += review.getDairyScore();
            dairyCount++;
            }
            if(!ObjectUtils.isEmpty(review.getEggScore())) {
            eggSum += review.getEggScore();
            eggCount++;
            }
        }

        int totalSum = peanutSum + dairySum + eggSum;
        int totalCount = peanutCount + dairyCount + eggCount;

        float overallScore = (float) totalSum / totalCount;
        restaurant.setOverallScore(decimalFormat.format(overallScore));

        if(peanutCount > 0) {
            float avgPeanutScore = (float) peanutSum / peanutCount;
            restaurant.setPeanutScore(decimalFormat.format(avgPeanutScore));
        }
        if(dairyCount > 0) {
            float avgDairyScore = (float) dairySum / dairyCount;
            restaurant.setDairyScore(decimalFormat.format(avgDairyScore));
        }
        if(eggCount > 0) {
            float avgEggScore = (float) eggSum / eggCount;
            restaurant.setEggScore(decimalFormat.format(avgEggScore));
        }
        restaurantRepository.save(restaurant);
    }
}
