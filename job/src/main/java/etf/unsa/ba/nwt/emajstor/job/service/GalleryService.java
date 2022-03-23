package etf.unsa.ba.nwt.emajstor.job.service;

import etf.unsa.ba.nwt.emajstor.job.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import etf.unsa.ba.nwt.emajstor.job.repositories.GalleryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GalleryService {

    private GalleryRepository galleryRepository;

    public List<Gallery> getAllGallery() {
        return galleryRepository.findAll();
    }

    public Gallery addGallery(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    public Gallery getGalleryById(UUID id) {
        return galleryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User with id " + id.toString() + " does not exist."));
    }

    public Gallery updateGalleryById(Gallery gallery,UUID id) {
        if (!galleryRepository.existsById(id)) {
            throw new BadRequestException("User with id " + id.toString() + " does not exist.");
        }

        return galleryRepository.save(gallery);
    }

    public Gallery deleteGalleryById(UUID id) {
        Gallery gallery = getGalleryById(id);
        galleryRepository.deleteById(id);

        return gallery;
    }

}
