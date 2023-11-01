package pro.sky.telegrambot.listener;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.component.SendNotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final TelegramBot telegramBot;
    private final NotificationTaskService notificationTaskService;
    private  final SendNotificationTask sendNotificationTask;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      NotificationTaskService notificationTaskService,
                                      SendNotificationTask sendNotificationTask) {
        this.telegramBot = telegramBot;
        this.notificationTaskService = notificationTaskService;
        this.sendNotificationTask = sendNotificationTask;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);
                String text = update.message().text();
                Long chatId = update.message().chat().id();

//              Обработка команды /start
                if ("/start".equals(text)) {
                    sendNotificationTask.sendMessage (chatId,
                            "Вас приветствует бот для планирования задач." +
                                    "Запрос передавать в формате: dd.MM.yyyy HH:mm 'текст задачи'");
                } else {
                    Matcher matcher = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)").matcher(text);
                    LocalDateTime localDateTime;

//                  Обработка полученной задачи
                    if (matcher.find() &&  (localDateTime = parse(matcher.group(1))) !=null) {
                        String message = matcher.group(3);
                        notificationTaskService.create(chatId, message, localDateTime);
                        sendNotificationTask.sendMessage(chatId, "Задача запланирована");
                    } else {
                        sendNotificationTask.sendMessage(chatId, "Некорректный формат сообщения!");
                    }
                }
            });
       return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    @Nullable
    private LocalDateTime parse(String localDateTime){
        try {
            return LocalDateTime.parse(localDateTime, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
