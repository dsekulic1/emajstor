package etf.unsa.ba.nwt.emajstor.review.seed;

import etf.unsa.ba.nwt.emajstor.review.model.Review;
import etf.unsa.ba.nwt.emajstor.review.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DatabaseSeeder {
    private final ReviewRepository reviewRepository;

    public DatabaseSeeder(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @EventListener
    public void seed(final ContextRefreshedEvent event) {
        seedDatabase();
    }

    private void seedDatabase() {
        if(reviewRepository.count() == 0) {

            createReview(3,"dobar majstor", UUID.fromString("f64cb9a4-b3e8-49d2-bd73-454a75f11d71"), UUID.fromString("50bfde38-7058-4d82-9854-ecc4609ae742"));
            createReview(1,"loš majstor", UUID.fromString("98434b78-f77b-4219-b363-1d883ebc691c"), UUID.fromString("50bfde38-7058-4d82-9854-ecc4609ae742"));
            createReview(2,"nije uradio kako je rekao da će uraditi", UUID.fromString("fe3a7b69-e13e-47c7-a60d-157d9f157bf1"), UUID.fromString("8e1eb86e-66e9-4a5d-a75b-470ab4cb70c8"));
            createReview(5,"odličan ! urađeno sve po dogovoru", UUID.fromString("4c7c7786-b086-4a12-87c3-3b6f7e644608"), UUID.fromString("a0c9de42-c32e-4134-a02b-0b0f40cd6b54"));
            createReview(2,"jako površan majstor, nije temeljit", UUID.fromString("a1788536-d2b8-4bc5-9609-45d33202058b"), UUID.fromString("8e1eb86e-66e9-4a5d-a75b-470ab4cb70c8"));
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