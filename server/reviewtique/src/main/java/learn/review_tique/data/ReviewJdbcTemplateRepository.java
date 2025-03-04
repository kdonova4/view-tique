package learn.review_tique.data;

import learn.review_tique.data.mappers.ReviewMapper;
import learn.review_tique.models.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ReviewJdbcTemplateRepository implements ReviewRepository{

    private final JdbcTemplate jdbcTemplate;

    public ReviewJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Review> findAll() {
        final String sql = "select review_id, score, review_time, review_body, likes, dislikes, app_user_id, game_id"
                + " from review;";
        return jdbcTemplate.query(sql, new ReviewMapper());
    }

    @Override
    public Review findById(int reviewId) {
        final String sql = "select review_id, score, review_time, review_body, likes, dislikes, app_user_id, game_id"
                + " from review "
                + "where review_id = ?;";
        return jdbcTemplate.query(sql, new ReviewMapper(), reviewId).stream().findFirst().orElse(null);
    }

    @Override
    public List<Review> findByUserId(int userId) {
        final String sql = "select review_id, score, review_time, review_body, likes, dislikes, r.app_user_id, r.game_id, g.title "
                + "from review r "
                + "inner join game g on g.game_id = r.game_id "
                + "where r.app_user_id = ?;";

        return jdbcTemplate.query(sql, new ReviewMapper(), userId);
    }

    @Override
    public List<Review> findByGameId(int gameId) {
        final String sql = "select review_id, score, review_time, review_body, likes, dislikes, r.app_user_id, r.game_id, g.title "
                + ", u.username "
                + "from review r "
                + "inner join app_user u on r.app_user_id = u.app_user_id "
                + "inner join game g on g.game_id = r.game_id "
                + "where g.game_id = ?;";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> {
            Review review = new Review();
            review.setReviewId((rs.getInt("review_id")));
            review.setScore(rs.getDouble("score"));
            review.setTimestamp(rs.getTimestamp("review_time"));
            review.setReviewBody(rs.getString("review_body"));
            review.setLikes(rs.getInt("likes"));
            review.setDislikes((rs.getInt("dislikes")));
            review.setUserId(rs.getInt("app_user_id"));
            review.setGameId(rs.getInt("game_id"));
            review.setUsername(rs.getString("username"));
            return review;
        }), gameId);
    }

    @Override
    public Review add(Review review) {
        final String sql = "insert into review (score, review_time, review_body, likes, dislikes, app_user_id, game_id)"
                + " values(?,?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, review.getScore());
            ps.setTimestamp(2, review.getTimestamp());
            ps.setString(3, review.getReviewBody());
            ps.setInt(4, review.getLikes());
            ps.setInt(5, review.getDislikes());
            ps.setInt(6, review.getUserId());
            ps.setInt(7, review.getGameId());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0) {
            return null;
        }

        review.setReviewId(keyHolder.getKey().intValue());
        return review;
    }

    @Override
    public boolean update(Review review) {
        final String sql = "update review set "
                + "score = ?, "
                + "review_time = ?, "
                + "review_body = ?, "
                + "likes = ?, "
                + "dislikes = ?, "
                + "app_user_id = ?, "
                + "game_id = ? "
                + "where review_id = ?;";

        return jdbcTemplate.update(sql, review.getScore(),
                review.getTimestamp(),
                review.getReviewBody(),
                review.getLikes(),
                review.getDislikes(),
                review.getUserId(),
                review.getGameId(),
                review.getReviewId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int reviewId) {
        final String reviewReactionSql = "delete from review_reaction where review_id = ?;";
        jdbcTemplate.update(reviewReactionSql, reviewId);
        final String reviewSql = "delete from review where review_id = ?;";

        return jdbcTemplate.update(reviewSql, reviewId) > 0;
    }
}
