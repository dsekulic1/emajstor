package etf.unsa.ba.nwt.emajstor.job.repositories;

import etf.unsa.ba.nwt.emajstor.job.model.Business;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BusinessRepository extends CrudRepository<Business, UUID> {
}
