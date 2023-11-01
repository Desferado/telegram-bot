/***
 * Интерфейс, с помощью которого можно сохранять и доставать из БД наши сущности.
 ***/
package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    // Метод, который ищет записи, у которых время совпадает с текущим
    List<NotificationTask> findAllByNotificationDateTime(LocalDateTime localDateTime);
}
