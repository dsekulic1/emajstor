package etf.unsa.ba.nwt.emajstor.job.repositories;


import etf.unsa.ba.nwt.emajstor.job.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {

}