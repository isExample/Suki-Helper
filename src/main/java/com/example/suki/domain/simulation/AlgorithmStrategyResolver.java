package com.example.suki.domain.simulation;

import com.example.suki.api.exception.BusinessException;
import com.example.suki.api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlgorithmStrategyResolver {
    private final List<AlgorithmStrategy> strategies;

    public AlgorithmStrategy find(AlgorithmType algorithmType) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(algorithmType))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.ALGORITHM_NOT_FOUND));
    }
}
