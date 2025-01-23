package learn.review_tique.data.mappers;

import learn.review_tique.models.ReactionType;
import learn.review_tique.models.ReviewReaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewReactionMapper implements RowMapper<ReviewReaction> {
    @Override
    public ReviewReaction mapRow(ResultSet resultSet, int i) throws SQLException {
        ReviewReaction reviewReaction = new ReviewReaction();

        reviewReaction.setReviewReactionId(resultSet.getInt("review_reaction_id"));
        reviewReaction.setReviewId(resultSet.getInt("review_id"));
        reviewReaction.setUserId(resultSet.getInt("app_user_id"));

        String reactionType = resultSet.getString("reaction_type");
        reviewReaction.setReactionType(ReactionType.valueOf(reactionType.toUpperCase()));
        return reviewReaction;
    }
}
