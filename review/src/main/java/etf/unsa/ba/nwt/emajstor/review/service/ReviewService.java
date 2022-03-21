package etf.unsa.ba.nwt.emajstor.review.service;

import etf.unsa.ba.nwt.emajstor.review.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

}
