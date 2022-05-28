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
@Table(name="job")
public class Job {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name="user_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID user;

    private String userName;

    private double price;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    private Business business;

//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "gallery_id", referencedColumnName = "id")
//    private Gallery gallery;

    public Job() {
    }

    public Job(UUID id, UUID user, String userName, double price, PriceType priceType, Business business) {
        this.id = id;
        this.user = user;
        this.userName = userName;
        this.price = price;
        this.priceType = priceType;
        this.business = business;
    }

    public Job(UUID user, String userName, double price, PriceType priceType, Business business) {
        this.user = user;
        this.userName = userName;
        this.price = price;
        this.priceType = priceType;
        this.business = business;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

//    public Gallery getGallery() {
//        return gallery;
//    }
//
//    public void setGallery(Gallery gallery) {
//        this.gallery = gallery;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
