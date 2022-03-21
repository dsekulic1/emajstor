package etf.unsa.ba.nwt.emajstor.review.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name="review")
public class Review {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
   // @Size(min=0, max=5)
    private int numStars;

    @Column(nullable = false)
    @NotBlank
    // @Size(min = 2, max = 255, message = "Comment can't be shorter than 2 chars or longer than 255")
    private String comment;

    @Column(name="user_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID user;

    @Column(name="worker_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID worker;

    public Review() {
    }

    public Review(int numStars, String comment, UUID user, UUID worker) {
        this.numStars = numStars;
        this.comment = comment;
        this.user = user;
        this.worker = worker;
    }

    public Review(UUID id, int numStars, String comment, UUID user, UUID worker) {
        this.id = id;
        this.numStars = numStars;
        this.comment = comment;
        this.user = user;
        this.worker = worker;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getNumStars() {
        return numStars;
    }

    public void setNumStars(int numStars) {
        this.numStars = numStars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public UUID getWorker() {
        return worker;
    }

    public void setWorker(UUID worker) {
        this.worker = worker;
    }
}
