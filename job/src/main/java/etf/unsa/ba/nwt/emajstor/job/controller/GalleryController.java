package etf.unsa.ba.nwt.emajstor.job.controller;

import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import etf.unsa.ba.nwt.emajstor.job.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping
    public ResponseEntity<Gallery> addBusiness(@RequestBody @Valid Gallery gallery) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Gallery> deleteGalleryById(@PathVariable UUID id) {
        return ResponseEntity.ok(galleryService.deleteGalleryById(id));
    }
}
