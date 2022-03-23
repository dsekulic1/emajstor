package etf.unsa.ba.nwt.emajstor.job.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="service")
public class Service {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name="user_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID user;

    private float price;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", referencedColumnName = "id", nullable = false)
    private Business business;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gallery_id", referencedColumnName = "id")
    private Gallery gallery;

    public Service() {
    }

    public Service(UUID user, float price, PriceType priceType, Business business, Gallery gallery) {
        this.user = user;
        this.price = price;
        this.priceType = priceType;
        this.business = business;
        this.gallery = gallery;
    }

    public Service(UUID id, UUID user, float price, PriceType priceType, Business business, Gallery gallery) {
        this.id = id;
        this.user = user;
        this.price = price;
        this.priceType = priceType;
        this.business = business;
        this.gallery = gallery;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
