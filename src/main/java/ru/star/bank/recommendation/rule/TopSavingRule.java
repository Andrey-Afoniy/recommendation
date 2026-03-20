package ru.star.bank.recommendation.rule;

import org.springframework.stereotype.Component;
import ru.star.bank.recommendation.dto.RecommendationDto;
import ru.star.bank.recommendation.repository.RecommendationRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRule implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "9cf94e79-c945-450f-b539-4f943704b970";
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String PRODUCT_TEXT = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, " +
            "который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\n" +
            "Преимущества «Копилки»:\n" +
            "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n" +
            "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n" +
            "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n" +
            "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final RecommendationRepository repository;

    public TopSavingRule(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasDebit = repository.hasProductType(userId, "DEBIT");
        BigDecimal debitDeposits = repository.sumAmountByTypeAndTransactionType(userId, "DEBIT", "DEPOSIT");
        BigDecimal savingDeposits = repository.sumAmountByTypeAndTransactionType(userId, "SAVING", "DEPOSIT");
        BigDecimal debitWithdrawals = repository.sumAmountByTypeAndTransactionType(userId, "DEBIT", "WITHDRAW");

        boolean condition2 = debitDeposits.compareTo(BigDecimal.valueOf(50000)) >= 0 ||
                savingDeposits.compareTo(BigDecimal.valueOf(50000)) >= 0;
        boolean condition3 = debitDeposits.compareTo(debitWithdrawals) > 0;

        if (hasDebit && condition2 && condition3) {
            return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
        }
        return Optional.empty();
    }
}