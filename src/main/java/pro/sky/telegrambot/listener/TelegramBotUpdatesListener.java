package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            Message message = update.message();
            if (message != null && message.text() != null) {
                String text = message.text();
                long chatId = message.chat().id();

                if ("/start".equals(text)) {
                    handleStartCommand(chatId);
                }
            }
        }
        return CONFIRMED_UPDATES_ALL;
    }
    private void handleStartCommand(long chatId) {
        String welcomeMessage = "Привет!!!";
        SendMessage sendMessage = new SendMessage(chatId, welcomeMessage);
        telegramBot.execute(sendMessage);
    }
}