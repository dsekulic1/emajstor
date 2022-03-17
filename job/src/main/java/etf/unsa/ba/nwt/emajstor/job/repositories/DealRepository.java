package etf.unsa.ba.nwt.emajstor.job.repositories;

import etf.unsa.ba.nwt.emajstor.job.model.Deal;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DealRepository extends CrudRepository<Deal, UUID> {
}
