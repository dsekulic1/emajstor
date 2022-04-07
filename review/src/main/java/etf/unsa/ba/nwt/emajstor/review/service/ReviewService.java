package etf.unsa.ba.nwt.emajstor.review.service;

import etf.unsa.ba.nwt.emajstor.review.dto.User;
import etf.unsa.ba.nwt.emajstor.review.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.review.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.review.model.Review;
import etf.unsa.ba.nwt.emajstor.review.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestTemplate restTemplate;

    public List<Review> getAllReviews() { return  reviewRepository.findAll(); }

    public Review getReviewById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Review with id " + id + " does not exist."));
    }

    public Review deleteReviewById(UUID id) {
        Review review = getReviewById(id);

        reviewRepository.deleteById(id);

        return review;
    }

    public User getUser(final UUID id) throws ServiceUnavailableException {
        try {
            return restTemplate.getForObject(
                    "http://user/api/users/{id}",
                    User.class,
                    id
            );
        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Error while communicating with another microservice.");
        }
    }

    public Review addReview(Review review) throws ServiceUnavailableException {
            if (validateReview(review)) {
                throw new BadRequestException("Review comment must contains text.");
            }

            try {
                if (getUser(review.getUser()) != null || getUser(review.getWorker()) != null) {
                    Review newReview= reviewRepository.save(review);
                    return newReview;
                } else {
                    throw new BadRequestException("User or worker does not exist.");
                }
            } catch (Exception exception) {
                throw exception;
            }
    }

    public Review updateReviewById(Review review, UUID id) {
        if (reviewRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Message with id " + id + " does not exist.");
        }

        return reviewRepository.save(review);
    }

    private Boolean validateReview (Review review) {
        return (StringUtils.isEmpty(review.getComment()) || StringUtils.isBlank(review.getComment()));
    }
}
