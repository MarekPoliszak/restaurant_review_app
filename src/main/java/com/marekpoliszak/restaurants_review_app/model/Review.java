package com.marekpoliszak.restaurants_review_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public record Review(
        @Id @GeneratedValue Long id,
        String submittedBy,
        String review,
        Integer peanutScore,
        Integer eggScore,
        Integer diaryScore,
        ReviewStatus status
) {}
