package ru.star.bank.recommendation.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.star.bank.recommendation.dto.RecommendationDto;
import ru.star.bank.recommendation.dto.User;
import ru.star.bank.recommendation.repository.RecommendationRepository;
import ru.star.bank.recommendation.service.RecommendationService;

import java.util.List;

@Component
public class RecommendationBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final RecommendationService recommendationService;
    private final RecommendationRepository userRepository;

    public RecommendationBot(@Value("${telegram.bot.token}") String botToken,
                             @Value("${telegram.bot.username}") String botUsername,
                             RecommendationService recommendationService,
                             RecommendationRepository userRepository) {
        super(botToken);
        this.botUsername = botUsername;
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMessage(chatId, "Привет! Я бот рекомендаций банка «Стар». Используйте /recommend <имя> для получения предложений.");
            } else if (messageText.startsWith("/recommend")) {
                String[] parts = messageText.split(" ", 2);
                if (parts.length < 2) {
                    sendMessage(chatId, "Пожалуйста, укажите имя пользователя. Пример: /recommend Иван Иванов");
                    return;
                }
                String userName = parts[1].trim();
                List<User> users = userRepository.findByNameIgnoreCase(userName);
                if (users.size() != 1) {
                    sendMessage(chatId, "Пользователь не найден");
                    return;
                }
                User user = users.get(0);
                List<RecommendationDto> recs = recommendationService.getRecommendations(user.getId());
                StringBuilder response = new StringBuilder("Здравствуйте ")
                        .append(user.getFirstName()).append(" ").append(user.getLastName())
                        .append("\nНовые продукты для вас:\n");
                if (recs.isEmpty()) {
                    response.append("— Пока нет рекомендаций.");
                } else {
                    for (RecommendationDto rec : recs) {
                        response.append("— ").append(rec.getName()).append("\n");
                    }
                }
                sendMessage(chatId, response.toString());
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}