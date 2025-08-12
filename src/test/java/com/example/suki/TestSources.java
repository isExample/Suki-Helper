package com.example.suki;

import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.trait.TraitCategory;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestSources {
    private TestSources(){}

    private static final List<PlaceCategory> DEFAULT_PLACES =
            Arrays.stream(PlaceCategory.values())
                    .filter(PlaceCategory::isDefault)
                    .toList();

    public static Stream<TraitCategory> 특성_스코프(Class<?> scopeType) {
        return Arrays.stream(TraitCategory.values())
                .filter(t -> scopeType.isInstance(t.getEffect()));
    }

    public static Stream<Arguments> 특성x기본장소(Stream<TraitCategory> traits) {
        return traits.flatMap(t -> DEFAULT_PLACES.stream().map(p -> Arguments.of(t, p)));
    }
}
