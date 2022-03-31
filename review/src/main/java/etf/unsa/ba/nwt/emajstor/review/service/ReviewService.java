package etf.unsa.ba.nwt.emajstor.review.service;

import etf.unsa.ba.nwt.emajstor.review.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.review.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.review.model.Review;
import etf.unsa.ba.nwt.emajstor.review.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

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

    public Review addReview(Review review) {
            if (validateReview(review)) {
                throw new BadRequestException("Review comment must contains text.");
            }

            try {
                Review newReview= reviewRepository.save(review);
                return newReview;
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
