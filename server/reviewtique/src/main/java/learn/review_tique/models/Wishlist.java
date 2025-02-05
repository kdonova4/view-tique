package learn.review_tique.models;

public class Wishlist {

    private int wishlistId;
    private int gameId;
    private int userId;

    public Wishlist() {

    }

    public Wishlist(int wishlistId, int gameId, int userId) {
        this.wishlistId = wishlistId;
        this.gameId = gameId;
        this.userId = userId;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
