package ru.star.bank.recommendation.service;

import org.springframework.stereotype.Service;
import ru.star.bank.recommendation.dto.RecommendationDto;
import ru.star.bank.recommendation.dynamic.entity.DynamicRule;
import ru.star.bank.recommendation.dynamic.entity.RuleQuery;
import ru.star.bank.recommendation.dynamic.repository.DynamicRuleRepository;
import ru.star.bank.recommendation.rule.RecommendationRuleSet;
import ru.star.bank.recommendation.dynamic.service.DynamicRuleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> staticRuleSets;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final RuleQueryExecutor queryExecutor;
    private final DynamicRuleService dynamicRuleService;  // новое

    public RecommendationService(List<RecommendationRuleSet> staticRuleSets,
                                 DynamicRuleRepository dynamicRuleRepository,
                                 RuleQueryExecutor queryExecutor,
                                 DynamicRuleService dynamicRuleService) {
        this.staticRuleSets = staticRuleSets;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.queryExecutor = queryExecutor;
        this.dynamicRuleService = dynamicRuleService;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        List<RecommendationDto> result = new ArrayList<>();

        // 1. Статические правила
        for (RecommendationRuleSet rule : staticRuleSets) {
            rule.evaluate(userId).ifPresent(result::add);
        }

        // 2. Динамические правила
        List<DynamicRule> dynamicRules = dynamicRuleRepository.findAll();
        for (DynamicRule rule : dynamicRules) {
            boolean ruleMatches = true;
            for (RuleQuery query : rule.getQueries()) {
                if (!queryExecutor.executeQuery(query, userId)) {
                    ruleMatches = false;
                    break;
                }
            }
            if (ruleMatches) {
                result.add(new RecommendationDto(rule.getProductName(), rule.getProductId(), rule.getProductText()));
                dynamicRuleService.incrementStats(rule.getId());   // <-- добавить
            }
        }

        return result;
    }
}