package etf.unsa.ba.nwt.emajstor.job.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="gallery")
public class Gallery {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String imageUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id", referencedColumnName = "id", nullable = false)
    private Service service;
}
