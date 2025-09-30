package com.example.suki;

import com.example.suki.api.validation.CoffeeMutuallyExclusiveValidator;
import com.example.suki.domain.item.ConsumableItemCategory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoffeeMutuallyExclusiveValidatorTest {
    private CoffeeMutuallyExclusiveValidator validator = new CoffeeMutuallyExclusiveValidator();

    @Test
    void 아이템_목록에_커피가_배타적으로_존재하면_유효하다() {
        Map<ConsumableItemCategory, Integer> itemMap = Map.of(ConsumableItemCategory.COFFEE, 1);

        boolean isValid = validator.isValid(itemMap, null);

        assertTrue(isValid);
    }

    @Test
    void 아이템_목록에_커피가_동시에_존재하면_유효하지_않다() {
        Map<ConsumableItemCategory, Integer> itemMap = Map.of(ConsumableItemCategory.COFFEE, 1, ConsumableItemCategory.COFFEE_X2, 1);

        boolean isValid = validator.isValid(itemMap, null);

        assertFalse(isValid);
    }
}
