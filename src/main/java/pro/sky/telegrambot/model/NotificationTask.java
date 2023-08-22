/***
 * Класс, который повторяет структуру нашей таблицы в БД;
 ***/

package pro.sky.telegrambot.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
public class NotificationTask {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private long userId;
    private String message;
    private LocalDateTime notificationDateTime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotificationDateTime(LocalDateTime notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }
}
