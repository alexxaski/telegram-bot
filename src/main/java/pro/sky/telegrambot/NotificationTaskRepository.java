package pro.sky.telegrambot;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    // В NotificationTaskRepository.java
    List<NotificationTask> findByScheduledTime(LocalDateTime time);

}