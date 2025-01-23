package learn.review_tique.data;

import learn.review_tique.models.ReviewReaction;

public interface ReviewReactionRepository {

    ReviewReaction findById(int reviewReactionId);

    ReviewReaction findByReviewIdAndUserId(int reviewReactionId, int userId);

    ReviewReaction add(ReviewReaction reviewReaction);

    boolean deleteById(int reviewReactionId);
}
