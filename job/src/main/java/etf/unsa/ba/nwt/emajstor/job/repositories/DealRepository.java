package etf.unsa.ba.nwt.emajstor.job.repositories;

import etf.unsa.ba.nwt.emajstor.job.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DealRepository extends JpaRepository<Deal, UUID> {

    boolean existsById(UUID id);
}
