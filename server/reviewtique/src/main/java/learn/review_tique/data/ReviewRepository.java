package learn.review_tique.data;

import learn.review_tique.models.Review;

import java.util.List;

public interface ReviewRepository {

    List<Review> findAll();

    Review findById(int reviewId);

    List<Review> findByUserId(int userId);

    List<Review> findByGameId(int gameId);

    Review add(Review review);

    boolean update(Review review);

    boolean deleteById(int reviewId);
}
