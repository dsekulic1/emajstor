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
            Message message1 = createMessage("Tekst prve poruke", UUID.fromString("4c7c7786-b086-4a12-87c3-3b6f7e644608"), UUID.fromString("a1788536-d2b8-4bc5-9609-45d33202058b"));
            Message message2 = createMessage("Tekst druge poruke", UUID.fromString("fbbc5958-96cc-4d3d-b050-2601ab7f5c72"), UUID.fromString("660a0de1-8267-49bb-ac12-ae58e8175af7"));
            Message message3 = createMessage("Tekst nte poruke", UUID.fromString("a0c9de42-c32e-4134-a02b-0b0f40cd6b54"), UUID.fromString("8e1eb86e-66e9-4a5d-a75b-470ab4cb70c8"));
            Message message4 = createMessage("Pozdrav, kako ste", UUID.fromString("50bfde38-7058-4d82-9854-ecc4609ae742"), UUID.fromString("f64cb9a4-b3e8-49d2-bd73-454a75f11d71"));
            Message message5 = createMessage("Da li ste slobodni u subotu", UUID.fromString("38913d69-7ac1-41c9-a637-5814d5ad334e"), UUID.fromString("fe3a7b69-e13e-47c7-a60d-157d9f157bf1"));
            Message message6 = createMessage("Samo da vam potvrdim dolazak", UUID.fromString("aebd23eb-3cd0-4247-b052-9e80ec9220e6"), UUID.fromString("f64cb9a4-b3e8-49d2-bd73-454a75f11d71"));
            createNotificationHistory(message1.getId(), message1, LocalDateTime.now());
            createNotificationHistory(message2.getId(), message2, LocalDateTime.now());
            createNotificationHistory(message3.getId(), message3, LocalDateTime.now());
            createNotificationHistory(message4.getId(), message4, LocalDateTime.now());
            createNotificationHistory(message5.getId(), message5, LocalDateTime.now());
            createNotificationHistory(message6.getId(), message6, LocalDateTime.now());
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
