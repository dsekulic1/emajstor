package etf.unsa.ba.nwt.emajstor.communication.seed;

import etf.unsa.ba.nwt.emajstor.communication.repositories.MessageRepository;
import etf.unsa.ba.nwt.emajstor.communication.repositories.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {
    private final MessageRepository messageRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;

    @EventListener
    public void seed(final ContextRefreshedEvent event) {
        seedDatabase();
    }

    private void seedDatabase() {
     // TODO SEED METHOD
    }

}
