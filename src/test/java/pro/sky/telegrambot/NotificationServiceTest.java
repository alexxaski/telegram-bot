package pro.sky.telegrambot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationTaskRepository notificationTaskRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testProcessReminderMessagePositiveScenario() {
        // Подготовительные данные
        String validMessage = "01.01.2022 20:00 Купить хлеб";
        Long chatId = 123456L;

        // Ожидаемые значения
        LocalDateTime expectedTime = LocalDateTime.parse("01.01.2022 20:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String expectedText = "Купить хлеб";

        // Выполняем тестируемый метод
        notificationService.processReminderMessage(validMessage, chatId);

        // Создаем ожидаемый объект
        NotificationTask expectedTask = new NotificationTask(chatId, expectedText, expectedTime);

        // Проверяем, что в репозиторий было передано нужное напоминание
        verify(notificationTaskRepository).save(expectedTask);
    }


    @Test
    public void testProcessReminderMessageNegativeScenario() {
        // Некорректное сообщение
        String invalidMessage = "Некорректное сообщение";
        Long chatId = 123456L;

        // Выполняем тестируемый метод
        notificationService.processReminderMessage(invalidMessage, chatId);

        // Проверяем, что ничего не было сохранено в репозиторий
        verify(notificationTaskRepository, never()).save(any(NotificationTask.class));

    }
}