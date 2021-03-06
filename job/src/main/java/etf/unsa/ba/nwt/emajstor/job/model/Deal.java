package etf.unsa.ba.nwt.emajstor.job.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="deal")
public class Deal {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name="user_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID user;

    private String name;

    private String email;

    private String number;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    private Job job;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime dateCreated;

    private Boolean finished;

    public Deal() {
    }

    public Deal(UUID user, String name, String email, String number, Job job, LocalDateTime dateCreated, Boolean finished) {
        this.user = user;
        this.name = name;
        this.email = email;
        this.number = number;
        this.job = job;
        this.dateCreated = dateCreated;
        this.finished = finished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Job getJob() { return job; }

    public void setJob(Job job) { this.job = job; }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
