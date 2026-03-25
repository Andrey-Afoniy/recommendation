package ru.star.bank.recommendation.dynamic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.star.bank.recommendation.dynamic.entity.RuleStats;
import java.util.UUID;

public interface RuleStatsRepository extends JpaRepository<RuleStats, UUID> {
}