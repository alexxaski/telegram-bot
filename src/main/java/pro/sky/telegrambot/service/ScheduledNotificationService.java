package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

        // Идет отправка уведомлений пользователям
        tasks.forEach(task -> {
            // Отправляем уведомления

            System.out.println("Отправляем сообщение: " + task.getNotificationText());
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

