package com.example.raeven.popularmovies.Model;

public class ReviewModel {
    private String reviewerName;
    private String review;

    public ReviewModel(String reviewerName, String review){
        this.reviewerName = reviewerName;
        this.review = review;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
