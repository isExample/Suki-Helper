package com.example.suki.domain.simulation.model;

import com.example.suki.domain.item.ConsumableItemCategory;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ConsumableBag {
    private final EnumMap<ConsumableItemCategory, Integer> remains;

    public ConsumableBag(Map<ConsumableItemCategory, Integer> initial) {
        this.remains = new EnumMap<>(ConsumableItemCategory.class);
        this.remains.putAll(initial);
    }

    public boolean canUse(ConsumableItemCategory item) {
        return this.remains.getOrDefault(item, 0) > 0;
    }

    public void use(ConsumableItemCategory item)  {
        this.remains.merge(item, -1, Integer::sum);
        if (this.remains.getOrDefault(item,0) <= 0) {
            this.remains.remove(item);
        }
    }

    public void undo(ConsumableItemCategory item) {
        this.remains.merge(item,  1, Integer::sum);
    }

    public Iterable<ConsumableItemCategory> usableItems() {
        return this.remains.keySet();
    }

    public Map<ConsumableItemCategory, Integer> snapshotRemains() {
        return Collections.unmodifiableMap(new EnumMap<>(remains));
    }
}
