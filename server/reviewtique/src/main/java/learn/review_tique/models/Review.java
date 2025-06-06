package learn.review_tique.models;

import java.sql.Timestamp;

public class Review {

    private int reviewId;
    private double score;
    private Timestamp timestamp;
    private String reviewBody;
    private int likes;
    private int dislikes;
    private int userId;
    private int gameId;
    private String username;

    public Review() {

    }

    public Review(int reviewId, double score, Timestamp timestamp, String reviewBody, int likes, int dislikes, int userId, int gameId) {
        this.reviewId = reviewId;
        this.score = score;
        this.timestamp = timestamp;
        this.reviewBody = reviewBody;
        this.likes = likes;
        this.dislikes = dislikes;
        this.userId = userId;
        this.gameId = gameId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
