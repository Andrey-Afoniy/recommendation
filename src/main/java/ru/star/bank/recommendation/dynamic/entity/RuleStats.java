package ru.star.bank.recommendation.dynamic.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "rule_stats")
public class RuleStats {

    @Id
    private UUID ruleId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "rule_id")
    private DynamicRule rule;

    @Column(nullable = false)
    private long count = 0;

    public RuleStats() {
    }

    public RuleStats(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public DynamicRule getRule() {
        return rule;
    }

    public void setRule(DynamicRule rule) {
        this.rule = rule;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}