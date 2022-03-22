package etf.unsa.ba.nwt.emajstor.communication.repositories;

import etf.unsa.ba.nwt.emajstor.communication.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List <Message> findAllByReceiver(UUID receiver);

    List <Message> findAllBySender(UUID sender);

}
