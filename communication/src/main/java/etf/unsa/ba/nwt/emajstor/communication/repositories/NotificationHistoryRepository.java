package etf.unsa.ba.nwt.emajstor.communication.repositories;

import etf.unsa.ba.nwt.emajstor.communication.model.NotificationHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface NotificationHistoryRepository extends CrudRepository<NotificationHistory, UUID> {
}
