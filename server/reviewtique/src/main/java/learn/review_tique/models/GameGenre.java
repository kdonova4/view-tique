package learn.review_tique.models;

public class GameGenre {

    private int gameId;
    private Genre genre;

    public GameGenre() {

    }

    public GameGenre(int gameId, Genre genre) {
        this.genre = genre;
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
