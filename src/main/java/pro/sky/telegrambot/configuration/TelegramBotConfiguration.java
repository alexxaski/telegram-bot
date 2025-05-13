package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.service.NotificationService;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;

@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
    @Component
    public static class TelegramBotInitializer {

        private final TelegramBot telegramBot;
        private final NotificationService notificationService;

        @Autowired
        public TelegramBotInitializer(TelegramBot telegramBot, NotificationService notificationService) {
            this.telegramBot = telegramBot;
            this.notificationService = notificationService;
        }

        @EventListener(ContextRefreshedEvent.class)
        public void setupBot(ContextRefreshedEvent event) {
            telegramBot.setUpdatesListener(new TelegramBotUpdatesListener(telegramBot, notificationService));
        }
    }
}
