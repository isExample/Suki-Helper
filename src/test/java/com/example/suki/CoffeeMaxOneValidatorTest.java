package com.example.suki;

import com.example.suki.api.validation.CoffeeMaxOneValidator;
import com.example.suki.domain.item.ConsumableItemCategory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoffeeMaxOneValidatorTest {
    private CoffeeMaxOneValidator validator = new CoffeeMaxOneValidator();

    @Test
    void 아이템_목록에_커피가_없으면_유효하다() {
        Map<ConsumableItemCategory, Integer> itemMap = Map.of(ConsumableItemCategory.CHOCOLATE_MILK, 1, ConsumableItemCategory.PUFFED_RICE, 2);

        boolean isValid = validator.isValid(itemMap, null);

        assertTrue(isValid);
    }

    @Test
    void 아이템_목록에_커피가_하나_있으면_유효하다() {
        Map<ConsumableItemCategory, Integer> itemMap1 = Map.of(ConsumableItemCategory.COFFEE, 1);
        Map<ConsumableItemCategory, Integer> itemMap2 = Map.of(ConsumableItemCategory.COFFEE_X2, 1);

        boolean isValid1 = validator.isValid(itemMap1, null);
        boolean isValid2 = validator.isValid(itemMap2, null);

        assertTrue(isValid1);
        assertTrue(isValid2);
    }

    @Test
    void 아이템_목록에_커피가_2개_이상_있으면_유효하지_않다() {
        Map<ConsumableItemCategory, Integer> itemMap1 = Map.of(ConsumableItemCategory.COFFEE, 2);
        Map<ConsumableItemCategory, Integer> itemMap2 = Map.of(ConsumableItemCategory.COFFEE_X2, 2);

        boolean isValid1 = validator.isValid(itemMap1, null);
        boolean isValid2 = validator.isValid(itemMap2, null);

        assertFalse(isValid1);
        assertFalse(isValid2);
    }

}
