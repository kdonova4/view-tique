package learn.review_tique.data.mappers;

import learn.review_tique.models.Review;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet resultSet, int i) throws SQLException {
        Review review = new Review();
        review.setReviewId((resultSet.getInt("review_id")));
        review.setScore(resultSet.getDouble("score"));
        review.setTimestamp(resultSet.getTimestamp("review_time"));
        review.setReviewBody(resultSet.getString("review_body"));
        review.setLikes(resultSet.getInt("likes"));
        review.setDislikes((resultSet.getInt("dislikes")));
        review.setUserId(resultSet.getInt("app_user_id"));
        review.setGameId(resultSet.getInt("game_id"));
        return review;
    }
}
