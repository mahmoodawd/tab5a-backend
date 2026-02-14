package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class MealRatingServiceImpl implements MealRatingService {
    private final MealRepository mealRepository;

    @Override
    public void onMealRatingAdded(Meal meal, BigDecimal rating) {
        if (rating.equals(BigDecimal.ZERO)) return;

        int currentCount = meal.getRatingCount();
        BigDecimal currentSum = meal.getRatingSum();

        int newCount = currentCount + 1;
        BigDecimal newSum = currentSum.add(rating);

        BigDecimal newAvg = newSum.divide(BigDecimal.valueOf(newCount), 2, RoundingMode.HALF_UP);


        meal.setRatingCount(newCount);
        meal.setRatingSum(newSum);
        meal.setRatingAvg(newAvg);
        mealRepository.save(meal);
    }

    @Override
    public void onMealRatingUpdated(Meal meal, BigDecimal newRating, BigDecimal oldRating) {

        if (newRating.equals(oldRating)) return;

        BigDecimal currentSum = meal.getRatingSum();
        int currentCount = meal.getRatingCount();
        BigDecimal newSum = currentSum.add(newRating.subtract(oldRating));

        BigDecimal newAvg = newSum.divide(BigDecimal.valueOf(currentCount), 2, RoundingMode.HALF_UP);

        meal.setRatingSum(newSum);
        meal.setRatingAvg(newAvg);
        mealRepository.save(meal);
    }

    @Override
    public void onMealRatingDeleted(Meal meal, BigDecimal rating) {
        if (rating.equals(BigDecimal.ZERO)) return;
        int newCount = meal.getRatingCount() - 1;
        BigDecimal newSum = meal.getRatingSum().subtract(rating);
        BigDecimal newAvg = newCount == 0 ? BigDecimal.ZERO :
                newSum.divide(BigDecimal.valueOf(newCount), 2, RoundingMode.HALF_UP);
        meal.setRatingSum(newSum);
        meal.setRatingAvg(newAvg);
        mealRepository.save(meal);

    }
}
