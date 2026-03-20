package ru.star.bank.recommendation.rule;

import org.springframework.stereotype.Component;
import ru.star.bank.recommendation.dto.RecommendationDto;
import ru.star.bank.recommendation.repository.RecommendationRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500Rule implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "9cf94e79-c945-450f-b539-4f943704b970";
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String PRODUCT_TEXT = "Откройте свой путь к успеху... (полный текст)";

    private final RecommendationRepository repository;

    public Invest500Rule(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasDebit = repository.hasProductType(userId, "DEBIT");
        boolean hasInvest = repository.hasProductType(userId, "INVEST");
        BigDecimal savingDeposits = repository.sumAmountByTypeAndTransactionType(userId, "SAVING", "DEPOSIT");

        if (hasDebit && !hasInvest && savingDeposits.compareTo(BigDecimal.valueOf(1000)) > 0) {
            return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
        }
        return Optional.empty();
    }
}