package com.example.suki.api.validation;

import com.example.suki.domain.item.ConsumableItemCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class CoffeeMutuallyExclusiveValidator implements ConstraintValidator<CoffeeMutuallyExclusive, Map<ConsumableItemCategory, Integer>> {
    @Override
    public boolean isValid(Map<ConsumableItemCategory, Integer> value, ConstraintValidatorContext context) {
        int coffee   = Math.max(0, value.getOrDefault(ConsumableItemCategory.COFFEE, 0));
        int coffeeX2 = Math.max(0, value.getOrDefault(ConsumableItemCategory.COFFEE_X2, 0));

        return (coffee == 0) || (coffeeX2 == 0);
    }
}
