package ru.star.bank.recommendation.rule;

import org.springframework.stereotype.Component;
import ru.star.bank.recommendation.dto.RecommendationDto;
import ru.star.bank.recommendation.repository.RecommendationRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRule implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "9cf94e79-c945-450f-b539-4f943704b970";
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String PRODUCT_TEXT = "Откройте мир выгодных кредитов с нами!\n\n" +
            "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! " +
            "Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\n" +
            "Почему выбирают нас:\n" +
            "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n" +
            "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n" +
            "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n" +
            "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final RecommendationRepository repository;

    public SimpleCreditRule(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasCredit = repository.hasProductType(userId, "CREDIT");
        BigDecimal debitDeposits = repository.sumAmountByTypeAndTransactionType(userId, "DEBIT", "DEPOSIT");
        BigDecimal debitWithdrawals = repository.sumAmountByTypeAndTransactionType(userId, "DEBIT", "WITHDRAW");

        if (!hasCredit && debitDeposits.compareTo(debitWithdrawals) > 0 && debitWithdrawals.compareTo(BigDecimal.valueOf(100000)) > 0) {
            return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
        }
        return Optional.empty();
    }
}