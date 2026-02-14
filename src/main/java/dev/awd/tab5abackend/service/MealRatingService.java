package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.model.Meal;

import java.math.BigDecimal;

public interface MealRatingService {

    void onMealRatingAdded(Meal meal, BigDecimal rating);

    void onMealRatingUpdated(Meal meal, BigDecimal newRating, BigDecimal oldRating);

    void onMealRatingDeleted(Meal meal, BigDecimal rating);
}
