package etf.unsa.ba.nwt.emajstor.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import etf.unsa.ba.nwt.emajstor.review.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.review.model.Review;
import etf.unsa.ba.nwt.emajstor.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.ServiceUnavailableException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody @Valid Review review) throws ServiceUnavailableException {
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

    // dodavavanje nove metode

    private Review applyPatchToReview(
            JsonPatch patch, Review targetReview) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetReview, JsonNode.class));
        return objectMapper.treeToValue(patched, Review.class);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            Review review = reviewService.getReviewById(UUID.fromString(id));
            Review reviewPatched = applyPatchToReview(patch, review);
            reviewService.updateReviewById(reviewPatched, reviewPatched.getId());
            return ResponseEntity.ok(reviewPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
