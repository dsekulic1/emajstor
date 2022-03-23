package etf.unsa.ba.nwt.emajstor.review.controller;

import etf.unsa.ba.nwt.emajstor.review.model.Review;
import etf.unsa.ba.nwt.emajstor.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody @Valid Review review) {
        return ResponseEntity.ok(reviewService.addReview(review));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Review> getReviewById (@PathVariable UUID id){
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Review> deleteMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewService.deleteReviewById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateMessageById(@PathVariable UUID id, @RequestBody @Valid Review review ) {
        return ResponseEntity.ok(reviewService.updateReviewById(review, id));
    }

}
