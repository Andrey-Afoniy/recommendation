package ru.star.bank.recommendation.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public boolean hasProductType(UUID userId, String productType) {
        String sql = """
            SELECT COUNT(*) > 0
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ?
        """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, userId.toString(), productType);
    }


    public BigDecimal sumAmountByTypeAndTransactionType(UUID userId, String productType, String transactionType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = ?
        """;
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId.toString(), productType, transactionType);
    }
}