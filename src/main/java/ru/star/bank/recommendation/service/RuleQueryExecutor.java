package ru.star.bank.recommendation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.star.bank.recommendation.dynamic.entity.RuleQuery;
import ru.star.bank.recommendation.repository.RecommendationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class RuleQueryExecutor {

    private final RecommendationRepository repository;
    private final ObjectMapper objectMapper;

    public RuleQueryExecutor(RecommendationRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public boolean executeQuery(RuleQuery query, UUID userId) {
        boolean result;
        List<String> args;
        try {
            args = objectMapper.readValue(query.getArguments(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse arguments", e);
        }

        switch (query.getQueryType()) {
            case "USER_OF":
                result = repository.hasProductType(userId, args.get(0));
                break;
            case "ACTIVE_USER_OF":
                result = repository.isActiveUserOfProductType(userId, args.get(0));
                break;
            case "TRANSACTION_SUM_COMPARE":
                result = compareSumWithConstant(userId, args);
                break;
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                result = compareDepositWithdraw(userId, args);
                break;
            default:
                throw new IllegalArgumentException("Unknown query type: " + query.getQueryType());
        }

        return query.isNegate() ? !result : result;
    }

    private boolean compareSumWithConstant(UUID userId, List<String> args) {

        String productType = args.get(0);
        String transactionType = args.get(1);
        String operator = args.get(2);
        BigDecimal constant = new BigDecimal(args.get(3));

        BigDecimal sum = repository.sumAmountByTypeAndTransactionType(userId, productType, transactionType);

        return compare(sum, operator, constant);
    }

    private boolean compareDepositWithdraw(UUID userId, List<String> args) {
        String productType = args.get(0);
        String operator = args.get(1);

        BigDecimal deposits = repository.sumAmountByTypeAndTransactionType(userId, productType, "DEPOSIT");
        BigDecimal withdrawals = repository.sumAmountByTypeAndTransactionType(userId, productType, "WITHDRAW");

        return compare(deposits, operator, withdrawals);
    }

    private boolean compare(BigDecimal left, String operator, BigDecimal right) {
        return switch (operator) {
            case ">" -> left.compareTo(right) > 0;
            case "<" -> left.compareTo(right) < 0;
            case "=" -> left.compareTo(right) == 0;
            case ">=" -> left.compareTo(right) >= 0;
            case "<=" -> left.compareTo(right) <= 0;
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }
}