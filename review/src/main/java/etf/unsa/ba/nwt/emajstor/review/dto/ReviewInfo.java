package etf.unsa.ba.nwt.emajstor.review.dto;

import etf.unsa.ba.nwt.emajstor.review.model.Review;

public class ReviewInfo {
    private User user;
    private User worker;
    private Review review;

    public ReviewInfo(User user, User worker, Review review) {
        this.user = user;
        this.worker = worker;
        this.review = review;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
