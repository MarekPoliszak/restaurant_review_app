package com.marekpoliszak.restaurants_review_app.repository;

import com.marekpoliszak.restaurants_review_app.model.Restaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    List<Restaurant> findRestaurantsByZipCodeAndPeanutScoreNotNullOrderedByPeanutScore(String zipCode);
    List<Restaurant> findRestaurantsByZipCodeAndDairyScoreNotNullOrderByDairyScore(String zipcode);
    List<Restaurant> findRestaurantsByZipCodeAndEggScoreNotNullOrderByEggScore(String zipcode);
    Optional<Restaurant> findRestaurantsByNameAndZipCode(String name, String zipCode);
}
