package etf.unsa.ba.nwt.emajstor.job.repositories;

import etf.unsa.ba.nwt.emajstor.job.model.Service;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ServiceRepository extends CrudRepository<Service, UUID> {
}
