package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.service.NotificationService;


import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final NotificationService notificationService;

    @Autowired
    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
    }


    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null && update.message().text() != null && !update.message().text().isEmpty()) {
                String messageText = update.message().text();
                Long chatId = update.message().chat().id();

                // Обрабатываем команды
                if (messageText.startsWith("/")) {
                    continue;
                }

                // Если сообщение не команда, отправляем ответ "ЩА!"
                SendMessage response = new SendMessage(chatId, "ЩА!");
                telegramBot.execute(response);

                // Если нужно обработать напоминания
                notificationService.processReminderMessage(messageText, chatId);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
