package etf.unsa.ba.nwt.emajstor.job.service;

import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import etf.unsa.ba.nwt.emajstor.job.repositories.GalleryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryService {

    private GalleryRepository galleryRepository;

    public List<Gallery> getAllGallery() {
        return galleryRepository.findAll();
    }

}
