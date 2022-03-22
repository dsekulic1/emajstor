package etf.unsa.ba.nwt.emajstor.communication.seed;

import etf.unsa.ba.nwt.emajstor.communication.model.Message;
import etf.unsa.ba.nwt.emajstor.communication.model.NotificationHistory;
import etf.unsa.ba.nwt.emajstor.communication.repositories.NotificationHistoryRepository;
import etf.unsa.ba.nwt.emajstor.communication.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class DatabaseSeeder {
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final MessageRepository messageRepository;

    @EventListener
    public void seed(final ContextRefreshedEvent event) {
        seedDatabase();
    }

    private void seedDatabase() {
        if(notificationHistoryRepository.count() == 0 && messageRepository.count() == 0) {
            Message message1 = createMessage("Tekst prve poruke", UUID.fromString("f64cb9a4-b3e8-49d2-bd73-454a75f11d71"), UUID.fromString("50bfde38-7058-4d82-9854-ecc4609ae742"));
            Message message2 = createMessage("Tekst druge poruke", UUID.fromString("50bfde38-7058-4d82-9854-ecc4609ae742"), UUID.fromString("f64cb9a4-b3e8-49d2-bd73-454a75f11d71"));
            createNotificationHistory(message1.getId(), message1, LocalDateTime.now());
            createNotificationHistory(message2.getId(), message2, LocalDateTime.now());
        }
    }

    private Message createMessage(String text, UUID sender, UUID reciver) {
        Message message = new Message();
        message.setText(text);
        message.setSender(sender);
        message.setReceiver(reciver);
        message = messageRepository.save(message);
        return message;
    }

    private NotificationHistory createNotificationHistory(UUID user, Message message, LocalDateTime timeStamp) {
        NotificationHistory notification = new NotificationHistory();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setTimeStamp(timeStamp);
        notification = notificationHistoryRepository.save(notification);
        return notification;
    }
}
