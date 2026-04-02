package ru.star.bank.recommendation.dynamic.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.star.bank.recommendation.dynamic.dto.RuleListResponse;
import ru.star.bank.recommendation.dynamic.dto.RuleRequest;
import ru.star.bank.recommendation.dynamic.dto.RuleResponse;
import ru.star.bank.recommendation.dynamic.dto.RuleStatsDto;
import ru.star.bank.recommendation.dynamic.entity.DynamicRule;
import ru.star.bank.recommendation.dynamic.entity.RuleStats;
import ru.star.bank.recommendation.dynamic.repository.RuleStatsRepository;
import ru.star.bank.recommendation.dynamic.service.DynamicRuleService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final DynamicRuleService ruleService;
    private final RuleStatsRepository ruleStatsRepository;

    public RuleController(DynamicRuleService ruleService, RuleStatsRepository ruleStatsRepository) {
        this.ruleService = ruleService;
        this.ruleStatsRepository = ruleStatsRepository;
    }

    @PostMapping
    public ResponseEntity<RuleResponse> createRule(@Valid @RequestBody RuleRequest request) {
        RuleResponse response = ruleService.createRule(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<RuleListResponse> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRulesResponse());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, List<RuleStatsDto>>> getStats() {
        List<DynamicRule> rules = ruleService.getAllRules();
        List<RuleStatsDto> stats = rules.stream()
                .map(rule -> {
                    long count = ruleStatsRepository.findById(rule.getId())
                            .map(RuleStats::getCount)
                            .orElse(0L);
                    return new RuleStatsDto(rule.getId(), count);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of("stats", stats));
    }
}