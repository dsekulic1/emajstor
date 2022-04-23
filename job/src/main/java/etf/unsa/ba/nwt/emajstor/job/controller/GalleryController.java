package etf.unsa.ba.nwt.emajstor.job.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import etf.unsa.ba.nwt.emajstor.job.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @PostMapping
    public ResponseEntity<Gallery> addGallery(@RequestBody @Valid Gallery gallery) {
        return ResponseEntity.ok(galleryService.addGallery(gallery));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Gallery>> getAllGallery() {
        return ResponseEntity.ok(galleryService.getAllGallery());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gallery> getGalleryById(@PathVariable UUID id) {
        return ResponseEntity.ok(galleryService.getGalleryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gallery> updateGalleryById(@PathVariable UUID id, @RequestBody @Valid Gallery gallery) {
        return ResponseEntity.ok(galleryService.updateGalleryById(gallery, id));
    }

    @PutMapping
    public ResponseEntity<Gallery> updateGallery(@RequestBody @Valid Gallery gallery) {
        return ResponseEntity.ok(galleryService.updateGalleryById(gallery, gallery.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Gallery> deleteGalleryById(@PathVariable UUID id) {
        return ResponseEntity.ok(galleryService.deleteGalleryById(id));
    }

    private Gallery applyPatchToGallery(
            JsonPatch patch, Gallery targetGallery) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetGallery, JsonNode.class));
        return objectMapper.treeToValue(patched, Gallery.class);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Gallery> updateGallery(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            Gallery gallery = galleryService.getGalleryById(UUID.fromString(id));
            Gallery galleryPatched = applyPatchToGallery(patch, gallery);
           galleryService.updateGalleryById(galleryPatched, galleryPatched.getId());
            return ResponseEntity.ok(galleryPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
