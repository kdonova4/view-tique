package learn.review_tique.models;

import java.time.LocalDate;
import java.util.List;

public class Game {

    private int gameId;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Developer developer;
    private double avgUserScore;
    private double avgCriticScore;
    private int userReviewCount;
    private int criticReviewCount;

    public Game() {

    }

    public Game(int gameId, String title, String description, LocalDate releaseDate, double avgUserScore, double avgCriticScore, int userReviewCount, int criticReviewCount, Developer developer) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.avgUserScore = avgUserScore;
        this.avgCriticScore = avgCriticScore;
        this.userReviewCount = userReviewCount;
        this.criticReviewCount = criticReviewCount;
        this.developer = developer;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public double getAvgUserScore() {
        return avgUserScore;
    }

    public void setAvgUserScore(double avgUserScore) {
        this.avgUserScore = Math.round(avgUserScore * 10.0) / 10.0;
    }

    public double getAvgCriticScore() {
        return avgCriticScore = Math.round(avgCriticScore * 10.0) / 10.0;
    }

    public void setAvgCriticScore(double avgCriticScore) {
        this.avgCriticScore = avgCriticScore;
    }

    public int getUserReviewCount() {
        return userReviewCount;
    }

    public void setUserReviewCount(int userReviewCount) {
        this.userReviewCount = userReviewCount;
    }

    public int getCriticReviewCount() {
        return criticReviewCount;
    }

    public void setCriticReviewCount(int criticReviewCount) {
        this.criticReviewCount = criticReviewCount;
    }
}
