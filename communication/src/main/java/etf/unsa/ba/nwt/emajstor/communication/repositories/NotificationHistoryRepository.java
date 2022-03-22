package etf.unsa.ba.nwt.emajstor.communication.repositories;

import etf.unsa.ba.nwt.emajstor.communication.model.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, UUID> {
}
