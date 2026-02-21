package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealRatingServiceImpl implements MealRatingService {

    private final MealRepository mealRepository;

    @Override
    public void onMealRatingAdded(Meal meal, BigDecimal rating) {
        Long mealId = meal.getId();

        log.info("Adding rating to meal. mealId={}, rating={}", mealId, rating);

        if (rating == null || rating.compareTo(BigDecimal.ZERO) == 0) {
            log.debug("Skipping zero/null rating. mealId={}", mealId);
            return;
        }

        int currentCount = meal.getRatingCount();
        BigDecimal currentSum = meal.getRatingSum();
        BigDecimal currentAvg = meal.getRatingAvg();

        int newCount = currentCount + 1;
        BigDecimal newSum = currentSum.add(rating);
        BigDecimal newAvg = newSum.divide(
                BigDecimal.valueOf(newCount),
                2,
                RoundingMode.HALF_UP
        );

        log.debug("Calculated new rating values. mealId={}, oldCount={}, newCount={}, oldAvg={}, newAvg={}",
                mealId, currentCount, newCount, currentAvg, newAvg);

        meal.setRatingCount(newCount);
        meal.setRatingSum(newSum);
        meal.setRatingAvg(newAvg);
        mealRepository.save(meal);

        log.info("Rating added successfully. mealId={}, ratingCount={}, ratingAvg={}",
                mealId, newCount, newAvg);
    }

    @Override
    public void onMealRatingUpdated(Meal meal, BigDecimal newRating, BigDecimal oldRating) {
        Long mealId = meal.getId();

        log.info("Updating meal rating. mealId={}, oldRating={}, newRating={}",
                mealId, oldRating, newRating);

        if (newRating != null && oldRating != null && newRating.compareTo(oldRating) == 0) {
            log.debug("Rating unchanged, skipping update. mealId={}, rating={}", mealId, newRating);
            return;
        }

        BigDecimal currentSum = meal.getRatingSum();
        BigDecimal currentAvg = meal.getRatingAvg();
        int currentCount = meal.getRatingCount();

        BigDecimal newSum = currentSum.add(newRating.subtract(oldRating));
        BigDecimal newAvg = newSum.divide(
                BigDecimal.valueOf(currentCount),
                2,
                RoundingMode.HALF_UP
        );

        log.debug("Calculated updated rating values. mealId={}, oldAvg={}, newAvg={}, ratingChange={}",
                mealId, currentAvg, newAvg, newRating.subtract(oldRating));

        meal.setRatingSum(newSum);
        meal.setRatingAvg(newAvg);
        mealRepository.save(meal);

        log.info("Rating updated successfully. mealId={}, ratingCount={}, ratingAvg={}",
                mealId, currentCount, newAvg);
    }

    @Override
    public void onMealRatingDeleted(Meal meal, BigDecimal rating) {
        Long mealId = meal.getId();

        log.info("Deleting rating from meal. mealId={}, rating={}", mealId, rating);

        // Skip zero/null ratings
        if (rating == null || rating.compareTo(BigDecimal.ZERO) == 0) {
            log.debug("Skipping zero/null rating deletion. mealId={}", mealId);
            return;
        }

        int currentCount = meal.getRatingCount();
        BigDecimal currentSum = meal.getRatingSum();
        BigDecimal currentAvg = meal.getRatingAvg();

        if (currentCount == 0) {
            log.warn("Attempted to delete rating from meal with no ratings. mealId={}", mealId);
            return;
        }

        int newCount = currentCount - 1;
        BigDecimal newSum = currentSum.subtract(rating);
        BigDecimal newAvg;

        if (newCount == 0) {
            log.debug("Last rating removed, resetting to zero. mealId={}", mealId);
            newAvg = BigDecimal.ZERO;
            meal.setRatingCount(0);
            meal.setRatingSum(BigDecimal.ZERO);
            meal.setRatingAvg(BigDecimal.ZERO);
        } else {
            newAvg = newSum.divide(
                    BigDecimal.valueOf(newCount),
                    2,
                    RoundingMode.HALF_UP
            );

            log.debug("Calculated new rating values after deletion. mealId={}, oldCount={}, newCount={}, oldAvg={}, newAvg={}",
                    mealId, currentCount, newCount, currentAvg, newAvg);

            meal.setRatingCount(newCount);
            meal.setRatingSum(newSum);
            meal.setRatingAvg(newAvg);
        }

        mealRepository.save(meal);

        log.info("Rating deleted successfully. mealId={}, ratingCount={}, ratingAvg={}",
                mealId, newCount, newAvg);
    }
}