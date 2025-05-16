package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {

    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    // Паттерн регулярного выражения для выделения даты и текста напоминания
    private Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");

    public void processReminderMessage(String inputMessage, Long chatId) {
        Matcher matcher = pattern.matcher(inputMessage);
        if (matcher.find()) {

            // Группа 1 — Дата и время
            String dateTimeStr = matcher.group(1);
            // Группа 3 — Текст напоминания
            String reminderText = matcher.group(3);

            // Парсим дату и время в LocalDateTime
            LocalDateTime scheduledTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

            // Создаем и сохраняем сущность
            NotificationTask task = new NotificationTask(chatId, reminderText, scheduledTime);
            notificationTaskRepository.save(task);
        } else {
            // Если регулярное выражение не сработало, сообщение игнорируем
            System.out.println("Не удалось распознать сообщение: " + inputMessage);
        }
    }
}