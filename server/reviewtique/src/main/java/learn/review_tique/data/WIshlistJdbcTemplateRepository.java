package learn.review_tique.data;

import learn.review_tique.data.mappers.WishlistMapper;
import learn.review_tique.models.Wishlist;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class WIshlistJdbcTemplateRepository implements WishlistRepository {

    private final JdbcTemplate jdbcTemplate;


    public WIshlistJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Wishlist> findByUserId(int userId) {
        final String sql = "select wishlist_id, game_id, app_user_id"
                + " from wishlist"
                + " where app_user_id = ?;";

        return jdbcTemplate.query(sql, new WishlistMapper(), userId);
    }

    @Override
    public Wishlist add(Wishlist wishlist) {
        final String sql = "insert into wishlist (game_id, app_user_id)"
                + " values (?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, wishlist.getGameId());
            ps.setInt(2, wishlist.getUserId());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0)
            return null;

        wishlist.setWishlistId(keyHolder.getKey().intValue());
        return wishlist;
    }

    @Override
    public boolean deleteById(int wishlistId) {
        final String sql = "delete from wishlist where wishlist_id = ?;";

        return jdbcTemplate.update(sql, wishlistId) > 0;
    }
}
