package learn.review_tique.models;

import java.time.LocalDate;
import java.util.List;

public class Game {

    private int gameId;
    private String title;
    private String description;
    private List<Platform> platforms;
    private LocalDate releaseDate;
    private Developer developer;
    private List<Genre> genres;
    private double totalUserScore;
    private double totalCriticScore;


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

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public double getTotalUserScore() {
        return totalUserScore;
    }

    public void setTotalUserScore(double totalUserScore) {
        this.totalUserScore = totalUserScore;
    }

    public double getTotalCriticScore() {
        return totalCriticScore;
    }

    public void setTotalCriticScore(double totalCriticScore) {
        this.totalCriticScore = totalCriticScore;
    }
}
