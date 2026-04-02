package ru.star.bank.recommendation.dynamic.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.star.bank.recommendation.dynamic.dto.RuleListResponse;
import ru.star.bank.recommendation.dynamic.dto.RuleRequest;
import ru.star.bank.recommendation.dynamic.dto.RuleResponse;
import ru.star.bank.recommendation.dynamic.entity.DynamicRule;
import ru.star.bank.recommendation.dynamic.entity.RuleStats;
import ru.star.bank.recommendation.dynamic.mapper.RuleMapper;
import ru.star.bank.recommendation.dynamic.repository.DynamicRuleRepository;
import ru.star.bank.recommendation.dynamic.repository.RuleStatsRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository ruleRepository;
    private final RuleMapper ruleMapper;
    private final RuleStatsRepository ruleStatsRepository;

    public DynamicRuleService(DynamicRuleRepository ruleRepository,
                              RuleMapper ruleMapper,
                              RuleStatsRepository ruleStatsRepository) {
        this.ruleRepository = ruleRepository;
        this.ruleMapper = ruleMapper;
        this.ruleStatsRepository = ruleStatsRepository;
    }

    @Transactional
    public RuleResponse createRule(RuleRequest request) {
        DynamicRule rule = ruleMapper.toEntity(request);
        rule = ruleRepository.save(rule);
        return ruleMapper.toResponse(rule);
    }

    @Transactional(readOnly = true)
    public List<DynamicRule> getAllRules() {
        return ruleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RuleListResponse getAllRulesResponse() {
        List<DynamicRule> rules = ruleRepository.findAll();
        List<RuleResponse> responses = rules.stream()
                .map(ruleMapper::toResponse)
                .collect(Collectors.toList());
        return new RuleListResponse(responses);
    }

    @Transactional
    public void deleteRule(UUID id) {
        ruleRepository.deleteById(id);
    }

    @Transactional
    public void incrementStats(UUID ruleId) {
        RuleStats stats = ruleStatsRepository.findById(ruleId).orElse(new RuleStats(ruleId));
        stats.setCount(stats.getCount() + 1);
        ruleStatsRepository.save(stats);
    }
}