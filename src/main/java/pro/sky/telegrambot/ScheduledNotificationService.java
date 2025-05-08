package pro.sky.telegrambot;
import java.time.temporal.ChronoUnit;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.NotificationTask;
import pro.sky.telegrambot.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledNotificationService {

    private final NotificationTaskRepository notificationTaskRepository;
    @Autowired
    private final TelegramBot telegramBot;
    public ScheduledNotificationService(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    // Шедулируем метод, который будет срабатывать каждые 1 минуту
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleNotifications() {
        // Текущее время обрезается до минуты (секунды становятся нулями)
        LocalDateTime currentMinute = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        // Получаем все записи, у которых время отправления равно текущей минуте
        List<NotificationTask> tasks = notificationTaskRepository.findByScheduledTime(currentMinute);
// Рассылаем уведомления
        sendNotifications(tasks);
        // Далее идет отправка уведомлений пользователям
        // Это можно реализовать в дополнительном методе или здесь же
        tasks.forEach(task -> {
            // Отправляем уведомления, например, через Telegram API
            System.out.println("Отправляем уведомление: " + task.getNotificationText());
        });
    }
    private void sendNotifications(List<NotificationTask> tasks) {
        for (NotificationTask task : tasks) {
            // Формируем сообщение
            SendMessage message = new SendMessage(task.getChatId().toString(), task.getNotificationText());

            // Отправляем сообщение
            telegramBot.execute(message);
        }
    }
}

