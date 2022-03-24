package etf.unsa.ba.nwt.emajstor.job.seed;

import etf.unsa.ba.nwt.emajstor.job.model.Business;
import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import etf.unsa.ba.nwt.emajstor.job.model.Job;
import etf.unsa.ba.nwt.emajstor.job.model.PriceType;
import etf.unsa.ba.nwt.emajstor.job.repositories.BusinessRepository;
import etf.unsa.ba.nwt.emajstor.job.repositories.GalleryRepository;
import etf.unsa.ba.nwt.emajstor.job.repositories.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final GalleryRepository galleryRepository;
    private final BusinessRepository businessRepository;
    private final JobRepository jobRepository;

    @EventListener
    public void seed(final ContextRefreshedEvent event) {
        seedDatabase();
    }

    private void seedDatabase() {
        if (galleryRepository.count() == 0 && businessRepository.count() == 0 && jobRepository.count() == 0) {
            Gallery gallery1 = createGallery("www.google.ba");
            Gallery gallery2 = createGallery("www.pexels.ba");

            Business business1 = createBusiness("Stolar");
            Business business2 = createBusiness("Keramicar");

            createJob(UUID.fromString("1dc0bec3-260a-4a50-8916-384128e8a55c"), 3.5, PriceType.PER_HOUR, business1, gallery1);
            createJob(UUID.fromString("8a351bab-e704-4afb-ab3e-7b27a54168a5"), 80, PriceType.PER_DAY, business2, gallery2);
        }
    }

    private Gallery createGallery(String imageUrl) {
        Gallery gallery = new Gallery();
        gallery.setImageUrl(imageUrl);
        gallery = galleryRepository.save(gallery);
        return gallery;
    }

    private Business createBusiness(String name) {
        Business business = new Business();
        business.setName(name);
        business = businessRepository.save(business);
        return business;
    }

    private Job createJob(UUID user, double price, PriceType priceType, Business business, Gallery gallery) {
        Job job = new Job();
        job.setUser(user);
        job.setPrice(price);
        job.setPriceType(priceType);
        job.setBusiness(business);
        job.setGallery(gallery);
        job = jobRepository.save(job);
        return job;
    }
}
