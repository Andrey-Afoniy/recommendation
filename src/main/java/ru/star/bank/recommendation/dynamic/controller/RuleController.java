package ru.star.bank.recommendation.dynamic.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.star.bank.recommendation.dynamic.dto.RuleListResponse;
import ru.star.bank.recommendation.dynamic.dto.RuleRequest;
import ru.star.bank.recommendation.dynamic.dto.RuleResponse;
import ru.star.bank.recommendation.dynamic.service.DynamicRuleService;

import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final DynamicRuleService ruleService;

    public RuleController(DynamicRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<RuleResponse> createRule(@Valid @RequestBody RuleRequest request) {
        RuleResponse response = ruleService.createRule(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<RuleListResponse> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}