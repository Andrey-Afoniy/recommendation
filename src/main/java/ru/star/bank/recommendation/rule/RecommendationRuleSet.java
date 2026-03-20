package ru.star.bank.recommendation.rule;

import ru.star.bank.recommendation.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> evaluate(UUID userId);
}