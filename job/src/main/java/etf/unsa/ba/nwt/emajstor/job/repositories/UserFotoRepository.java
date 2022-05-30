package etf.unsa.ba.nwt.emajstor.job.repositories;

import etf.unsa.ba.nwt.emajstor.job.model.UserFoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserFotoRepository extends JpaRepository<UserFoto, UUID> {
    Optional<UserFoto> findUserFotoByUserId(UUID userId);

}
