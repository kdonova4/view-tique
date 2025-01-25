package learn.review_tique.data;

import learn.review_tique.data.mappers.ReviewReactionMapper;
import learn.review_tique.models.ReactionType;
import learn.review_tique.models.ReviewReaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class ReviewReactionJdbcTemplateRepository implements ReviewReactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewReactionJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReviewReaction findById(int reviewReactionId) {
        final String sql = "select review_reaction_id, review_id, app_user_id, reaction_type"
                + " from review_reaction "
                + "where review_reaction_id = ?;";

        return jdbcTemplate.query(sql, new ReviewReactionMapper(), reviewReactionId).stream().findFirst().orElse(null);
    }

    @Override
    public ReviewReaction findByReviewIdAndUserId(int reviewId, int userId) {
        final String sql = "select review_reaction_id, review_id, app_user_id, reaction_type"
                + " from review_reaction "
                + " where review_id = ? and app_user_id = ?;";

        return jdbcTemplate.query(sql, new ReviewReactionMapper(), reviewId, userId).stream().findFirst().orElse(null);
    }

    @Override
    public ReviewReaction add(ReviewReaction reviewReaction) {
        final String sql = "insert into review_reaction (review_id, app_user_id, reaction_type)"
                + " values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, reviewReaction.getReviewId());
            ps.setInt(2, reviewReaction.getUserId());
            ps.setString(3, reviewReaction.getReactionType().toString().toLowerCase());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0)
            return null;

        reviewReaction.setReviewReactionId(keyHolder.getKey().intValue());
        return reviewReaction;

    }

    @Override
    public boolean deleteById(int reviewReactionId) {
        final String sql = "delete from review_reaction where review_reaction_id = ?;";

        return jdbcTemplate.update(sql, reviewReactionId) > 0;
    }
}
