package etf.unsa.ba.nwt.emajstor.review.seed;

import etf.unsa.ba.nwt.emajstor.review.model.Review;
import etf.unsa.ba.nwt.emajstor.review.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {
    private final ReviewRepository reviewRepository;

    @EventListener
    public void seed(final ContextRefreshedEvent event) {
        seedDatabase();
    }

    private void seedDatabase() {
        if(reviewRepository.count() == 0) {

            createReview(3,"dobar majstor", UUID.fromString("f64cb9a4-b3e8-49d2-bd73-454a75f11d71"), UUID.fromString("50bfde38-7058-4d82-9854-ecc4609ae742"));
            createReview(1,"loš majstor", UUID.fromString("98434b78-f77b-4219-b363-1d883ebc691c"), UUID.fromString("50bfde38-7058-4d82-9854-ecc4609ae742"));
        }
    }

    private Review createReview(int numStars, String comment, UUID user, UUID worker) {
       Review review = new Review();
       review.setNumStars(numStars);
       review.setComment(comment);
       review.setUser(user);
       review.setWorker(worker);
       review = reviewRepository.save(review);

       return review;
    }


}