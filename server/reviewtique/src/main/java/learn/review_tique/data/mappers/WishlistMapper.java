package learn.review_tique.data.mappers;

import learn.review_tique.models.Wishlist;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WishlistMapper implements RowMapper<Wishlist> {
    @Override
    public Wishlist mapRow(ResultSet resultSet, int i) throws SQLException {
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(resultSet.getInt("wishlist_id"));
        wishlist.setGameId(resultSet.getInt("game_id"));
        wishlist.setUserId(resultSet.getInt("app_user_id"));
        return wishlist;
    }
}
