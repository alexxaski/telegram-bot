package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.ScheduledNotificationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ScheduledNotificationServiceTest {

    @Mock
    private NotificationTaskRepository notificationTaskRepository;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private ScheduledNotificationService scheduledNotificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testScheduleNotifications() {
        // Подготовительные данные
        LocalDateTime now = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
        NotificationTask task1 = new NotificationTask(1L, "123456",now );
        NotificationTask task2 = new NotificationTask(2L, "654321",now);

        // Моки репозитория
        List<NotificationTask> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        when(notificationTaskRepository.findByScheduledTime(now)).thenReturn(tasks);

        // Выполняем тестируемый метод
        scheduledNotificationService.scheduleNotifications();

        // Проверяем, что оба уведомления были отправлены
        verify(telegramBot, times(2)).execute(any(SendMessage.class));
    }


    @Test
    public void testNoTasksForCurrentTime() {
        // Подготовительные данные
        LocalDateTime now = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);

        // Моки репозитория
        when(notificationTaskRepository.findByScheduledTime(now)).thenReturn(new ArrayList<>());

        // Выполняем тестируемый метод
        scheduledNotificationService.scheduleNotifications();

        // Проверяем, что никакие уведомления не были отправлены
        verify(telegramBot, never()).execute(any(SendMessage.class));
    }
}
