package etf.unsa.ba.nwt.emajstor.user.repository;

import etf.unsa.ba.nwt.emajstor.user.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, UUID> {

    boolean existsByEmail(String email);

    Optional<ContactInfo> findByEmail(String email);
}
