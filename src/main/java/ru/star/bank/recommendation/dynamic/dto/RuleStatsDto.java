package ru.star.bank.recommendation.dynamic.dto;

import java.util.UUID;

public class RuleStatsDto {
    private UUID ruleId;
    private long count;

    public RuleStatsDto(UUID ruleId, long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}