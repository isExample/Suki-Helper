package com.example.suki.domain.simulation.model;

import com.example.suki.domain.item.ConsumableItemCategory;

import java.util.EnumMap;
import java.util.Map;

public class ConsumableBag {
    private final EnumMap<ConsumableItemCategory, Integer> remains;

    public ConsumableBag(Map<ConsumableItemCategory, Integer> initial) {
        this.remains = new EnumMap<>(ConsumableItemCategory.class);
        this.remains.putAll(initial);
    }

    boolean canUse(ConsumableItemCategory item) {
        return this.remains.getOrDefault(item, 0) > 0;
    }

    void use(ConsumableItemCategory item)  {
        this.remains.merge(item, -1, Integer::sum);
        if (this.remains.getOrDefault(item,0) <= 0) {
            this.remains.remove(item);
        }
    }

    void undo(ConsumableItemCategory item) {
        this.remains.merge(item,  1, Integer::sum);
    }

    Iterable<ConsumableItemCategory> usableItems() {
        return this.remains.keySet();
    }
}
