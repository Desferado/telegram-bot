/***
 * * Данный класс осуществляет
 * поиск задач на текущее время и рассылку уведомлений в нужные чаты;
 *
 ***/



package pro.sky.telegrambot.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;


@Component
public class NotificationTaskScheduled {
    private final NotificationTaskRepository notificationTaskRepository;
    private final SendNotificationTask sendNotificationTask;


    public NotificationTaskScheduled(NotificationTaskRepository notificationTaskRepository, SendNotificationTask sendNotificationTask) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.sendNotificationTask = sendNotificationTask;
    }
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void task(){
        notificationTaskRepository.findAllByNotificationDateTime(
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        ).forEach(notificationTask -> {
            sendNotificationTask.sendMessage(notificationTask.getUserId(), notificationTask.getMessage());
            notificationTaskRepository.delete(notificationTask);
        });
    }
}
