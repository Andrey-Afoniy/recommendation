package ru.star.bank.recommendation.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.star.bank.recommendation.dto.User;
import java.util.List;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(@Qualifier("defaultJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable(value = "hasProductType", key = "{#userId, #productType}")
    public boolean hasProductType(UUID userId, String productType) {
        String sql = """
                    SELECT COUNT(*) > 0
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ?
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, userId.toString(), productType);
    }

    @Cacheable(value = "isActiveUserOfProductType", key = "{#userId, #productType}")
    public boolean isActiveUserOfProductType(UUID userId, String productType) {
        String sql = """
                    SELECT COUNT(*) >= 5
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ?
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, userId.toString(), productType);
    }

    @Cacheable(value = "sumAmountByTypeAndTransactionType", key = "{#userId, #productType, #transactionType}")
    public BigDecimal sumAmountByTypeAndTransactionType(UUID userId, String productType, String transactionType) {
        String sql = """
                    SELECT COALESCE(SUM(t.amount), 0)
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ? AND t.type = ?
                """;
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId.toString(), productType, transactionType);
    }

    public List<User> findByNameIgnoreCase(String fullName) {
        String sql = """
                    SELECT id, first_name, last_name
                    FROM users
                    WHERE CONCAT(first_name, ' ', last_name) ILIKE ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                UUID.fromString(rs.getString("id")),
                rs.getString("first_name"),
                rs.getString("last_name")
        ), "%" + fullName + "%");
    }
}