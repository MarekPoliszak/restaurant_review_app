package com.marekpoliszak.restaurants_review_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public record User(
        @Id @GeneratedValue Long id,
        String displayName,
        String city,
        String state,
        String zipCode,
        Boolean peanutWatch,
        Boolean dairyWatch,
        Boolean eggWatch
) {}
