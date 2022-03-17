package etf.unsa.ba.nwt.emajstor.job.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name="business")
public class Business {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @OneToOne(mappedBy = "business")
    private Service service;

    public Business() {
    }

    public Business(String name) {
        this.name = name;
    }

    public Business(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
