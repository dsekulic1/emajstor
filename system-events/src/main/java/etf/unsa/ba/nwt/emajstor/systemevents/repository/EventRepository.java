package etf.unsa.ba.nwt.emajstor.systemevents.repository;

import etf.unsa.ba.nwt.emajstor.systemevents.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
