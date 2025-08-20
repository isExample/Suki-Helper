package com.example.suki.domain.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.IntUnaryOperator;

@Getter
@RequiredArgsConstructor
public enum ConsumableItemCategory {
    DECAF_COFFEE("디카페인 커피", s -> s + 15),
    PUFFED_RICE("뻥튀기", s -> s * 2),
    ROYAL_FEAST("수라상", s -> s + 100),
    CHOCOLATE_MILK("초코우유", s -> s + 50),
    COFFEE("커피", s -> s + 25),                 // 하루 1개 제한
    WHITE_MILK("흰 우유", s -> s + 30);

    private final String name;
    private final IntUnaryOperator effect;
}
