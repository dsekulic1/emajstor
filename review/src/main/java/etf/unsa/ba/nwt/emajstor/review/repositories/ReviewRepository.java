package etf.unsa.ba.nwt.emajstor.review.repositories;

import etf.unsa.ba.nwt.emajstor.review.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ReviewRepository extends CrudRepository<Review, UUID> {
}
