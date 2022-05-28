package etf.unsa.ba.nwt.emajstor.job.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.FileEntity;
import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import etf.unsa.ba.nwt.emajstor.job.repositories.FileRepository;
import etf.unsa.ba.nwt.emajstor.job.response.FileResponse;
import etf.unsa.ba.nwt.emajstor.job.service.FileService;
import etf.unsa.ba.nwt.emajstor.job.service.GalleryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/gallery")
public class GalleryController {
    private static final Logger logger = LoggerFactory.getLogger(GalleryController.class);

    private final GalleryService galleryService;
    private final FileService fileService;

    public GalleryController(GalleryService galleryService, FileService fileService) {
        this.galleryService = galleryService;
        this.fileService = fileService;
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileEntity uploadFile(@RequestParam MultipartFile file) throws IOException {
        logger.warn(String.format("File name '%s' uploaded successfully.", file.getOriginalFilename()));

        return fileService.save(file);

    }

    @GetMapping(path = "/allimages")
    public List<FileResponse> list() {
//        return fileService.getAllFiles()
//                .stream()
//                .map(this::mapToFileResponse)
//                .collect(Collectors.toList());
        return galleryService.getAllGallery()
                .stream()
                .map(gallery -> mapToFileResponse(gallery.getFileEntity(), gallery.getJobId()))
                .collect(Collectors.toList());
    }


    private FileResponse mapToFileResponse(FileEntity fileEntity, UUID jobId) {
        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileEntity.getId())
                .toUriString();
        FileResponse fileResponse = new FileResponse();
        fileResponse.setId(fileEntity.getId());
        fileResponse.setName(fileEntity.getName());
        fileResponse.setContentType(fileEntity.getContentType());
        fileResponse.setSize(fileEntity.getSize());
        fileResponse.setUrl(downloadURL);
        fileResponse.setBase64(Base64.getEncoder().encodeToString(fileEntity.getData()));
        fileResponse.setJobId(jobId);

        return fileResponse;
    }

}
