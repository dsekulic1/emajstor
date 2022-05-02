package etf.unsa.ba.nwt.emajstor.communication.repositories;

import etf.unsa.ba.nwt.emajstor.communication.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
}
