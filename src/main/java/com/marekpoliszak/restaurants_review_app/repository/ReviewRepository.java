package com.marekpoliszak.restaurants_review_app.repository;

import com.marekpoliszak.restaurants_review_app.model.Review;
import com.marekpoliszak.restaurants_review_app.model.ReviewStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findReviewsByStatus(ReviewStatus reviewStatus);
    List<Review> findReviewsByRestaurantIdAndStatus(Long restaurantId, ReviewStatus reviewStatus);
}
