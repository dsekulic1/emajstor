package etf.unsa.ba.nwt.emajstor.job.repositories;

import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GalleryRepository extends CrudRepository<Gallery, UUID> {
}
