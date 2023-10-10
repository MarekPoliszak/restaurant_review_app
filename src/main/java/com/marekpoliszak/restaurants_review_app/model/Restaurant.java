package com.marekpoliszak.restaurants_review_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public record Restaurant(
        @Id @GeneratedValue Long id,
        String name,
        String line1,
        String city,
        String state,
        String zipCode,
        String phoneNumber,
        String website,
        String overallScore,
        String peanutScore,
        String dairyScore,
        String eggScore
) {}
