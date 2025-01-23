package learn.review_tique.models;

public class ReviewReaction {

    private int reviewReactionId;
    private int reviewId;
    private int userId;
    private ReactionType reactionType;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getReviewReactionId() {
        return reviewReactionId;
    }

    public void setReviewReactionId(int reviewReactionId) {
        this.reviewReactionId = reviewReactionId;
    }
}
