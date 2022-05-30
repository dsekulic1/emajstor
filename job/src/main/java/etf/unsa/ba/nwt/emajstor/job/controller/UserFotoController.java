package etf.unsa.ba.nwt.emajstor.job.controller;

import etf.unsa.ba.nwt.emajstor.job.model.FileEntity;
import etf.unsa.ba.nwt.emajstor.job.model.UserFoto;
import etf.unsa.ba.nwt.emajstor.job.service.FileService;
import etf.unsa.ba.nwt.emajstor.job.service.UserFotoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/userfoto")
public class UserFotoController {
    private final FileService fileService;
    private final UserFotoService userFotoService;

    public UserFotoController(FileService fileService, UserFotoService userFotoService) {
        this.fileService = fileService;
        this.userFotoService = userFotoService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileEntity uploadFile(@RequestParam MultipartFile file) throws IOException {
        return fileService.save(file);
    }

    @PostMapping
    public ResponseEntity<UserFoto> addGallery(@RequestBody @Valid UserFoto userFoto) {
        return ResponseEntity.ok(userFotoService.addUserFoto(userFoto));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<UserFoto>> getAllUserFoto() {
        return ResponseEntity.ok(userFotoService.getAllUserFoto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFoto> getUserFotoById(@PathVariable UUID id) {
        return ResponseEntity.ok(userFotoService.getUserFotoById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserFoto> updateUserFotoById(@PathVariable UUID id, @RequestBody @Valid UserFoto userFoto) {
        return ResponseEntity.ok(userFotoService.updateUserFotoById(userFoto, id));
    }
}
