package etf.unsa.ba.nwt.emajstor.communication.repositories;

import etf.unsa.ba.nwt.emajstor.communication.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message, UUID> {
}
