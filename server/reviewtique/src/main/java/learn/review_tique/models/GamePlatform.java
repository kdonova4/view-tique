package learn.review_tique.models;

public class GamePlatform {

    private int gameId;
    private Platform platform;

    public GamePlatform() {

    }

    public GamePlatform(int gameId, Platform platform) {
        this.gameId = gameId;
        this.platform = platform;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}
