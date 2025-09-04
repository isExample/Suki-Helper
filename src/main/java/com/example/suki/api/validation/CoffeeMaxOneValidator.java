package com.example.suki.api.validation;

import com.example.suki.domain.item.ConsumableItemCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class CoffeeMaxOneValidator implements ConstraintValidator<CoffeeMaxOne, Map<ConsumableItemCategory, Integer>> {
    @Override
    public boolean isValid(Map<ConsumableItemCategory, Integer> value, ConstraintValidatorContext context) {
        Integer coffee = value.get(ConsumableItemCategory.COFFEE);
        Integer coffeeX2 = value.get(ConsumableItemCategory.COFFEE_X2);

        return (coffee == null || coffee <= 1) && (coffeeX2 == null || coffeeX2 <= 1);
    }
}
