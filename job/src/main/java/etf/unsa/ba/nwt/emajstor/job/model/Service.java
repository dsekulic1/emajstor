package etf.unsa.ba.nwt.emajstor.job.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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

    private PriceType priceType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_id", referencedColumnName = "id", nullable = false)
    private Business business;

    @OneToOne(mappedBy = "service")
    private Gallery gallery;

    @OneToOne(mappedBy = "service")
    private Deal deal;

    public Service() {
    }

    public Service(UUID user, float price, PriceType priceType, Business business) {
        this.user = user;
        this.price = price;
        this.priceType = priceType;
        this.business = business;
    }

    public Service(UUID id, UUID user, float price, PriceType priceType, Business business) {
        this.id = id;
        this.user = user;
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
}
