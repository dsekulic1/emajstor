package etf.unsa.ba.nwt.emajstor.communication.dto;

import java.util.UUID;

public class Review {
    private UUID id;
    private int numStars;
    private String comment;
    private UUID user;
    private UUID worker;

    public Review() {
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

