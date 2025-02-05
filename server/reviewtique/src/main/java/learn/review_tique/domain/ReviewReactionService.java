package learn.review_tique.domain;

import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.ReviewReactionRepository;
import learn.review_tique.data.ReviewRepository;
import learn.review_tique.models.ReactionType;
import learn.review_tique.models.Review;
import learn.review_tique.models.ReviewReaction;
import org.springframework.stereotype.Service;

/*
        Review Reaction Data
        ============
        - ReviewReaction reviewId
            - reviewId MUST be VALID and EXIST
        - ReviewReaction userId
            - userId MUST be VALID and EXIST
        - ReviewReaction ReactionType
            - reactionType MUST NOT BE NULL



        public ReviewReaction findById(int reviewReactionId)
            // return repo findById() method

        public ReviewReaction findByReviewIdAndUserId(int reviewId, int userId)
            // return repo findByReviewIdAndUserId() method

        public Result<ReviewReaction> add(ReviewReaction reviewReaction)
            // validate
            // if validate is not successful, then add message and type and return result
            // if ID is set, add message and type and return result
            // set ReviewReaction to repo.add() method
            // set payload to the returned ReviewReaction
            // if reaction type is like then add 1 to review repo setLikes()
            // if reaction type is like then add 1 to review repo setDislikes()
            // return result

        public boolean deleteById(int genreId)
            // if reaction type is like then subtract 1 to review repo setLikes()
            // if reaction type is like then subtract 1 to review repo setDislikes()
            //return repo call to deleteById()

        private Result<Genre> validate(Genre genre)
            // new result
            // check if ReviewReaction is null, if it is then add message and type and return result
            // check if reviewId is NOT VALID, if not add message and type
            // check if userId is VALID, if not add message and type
            // return result

     */
@Service
public class ReviewReactionService {

    private final ReviewReactionRepository reactionRepository;
    private final ReviewRepository reviewRepository;
    private final AppUserRepository appUserRepository;

    public ReviewReactionService(ReviewReactionRepository reactionRepository, ReviewRepository reviewRepository,
                                 AppUserRepository appUserRepository) {
        this.reactionRepository = reactionRepository;
        this.reviewRepository = reviewRepository;
        this.appUserRepository = appUserRepository;
    }

    public ReviewReaction findById(int reviewReactionId) {
        return reactionRepository.findById(reviewReactionId);
    }

    public ReviewReaction findByReviewAndUserId(int reviewId, int userId) {
        return reactionRepository.findByReviewIdAndUserId(reviewId, userId);
    }

    public Result<ReviewReaction> add(ReviewReaction reviewReaction) {
        Result<ReviewReaction> result = validate(reviewReaction);

        if(!result.isSuccess())
            return result;

        if(reviewReaction.getReviewReactionId() != 0) {
            result.addMessages("ReviewReaction ID CANNOT BE SET", ResultType.INVALID);
            return result;
        }

        reviewReaction = reactionRepository.add(reviewReaction);
        result.setPayload(reviewReaction);

        Review review = reviewRepository.findById(reviewReaction.getReviewId());
        if(reviewReaction.getReactionType() == ReactionType.LIKE) {
            review.setLikes(review.getLikes() + 1);
        } else {
            review.setDislikes(review.getDislikes() + 1);
        }
        reviewRepository.update(review);
        return result;
    }

    public boolean deleteById(int reviewReactionId) {
        ReviewReaction reviewReaction = reactionRepository.findById(reviewReactionId);
        if(reviewReaction == null)
            return false;

        Review review = reviewRepository.findById(reviewReaction.getReviewId());
        if(review == null)
            return false;

        if(reviewReaction.getReactionType() == ReactionType.LIKE) {
            review.setLikes(review.getLikes() - 1);
        } else {
            review.setDislikes(review.getDislikes() - 1);
        }

        reviewRepository.update(review);
        return reactionRepository.deleteById(reviewReactionId);
    }

    private Result<ReviewReaction> validate(ReviewReaction reviewReaction) {
        Result<ReviewReaction> result = new Result<>();

        if(reviewReaction == null) {
            result.addMessages("ReviewReaction CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(reviewReaction.getReactionType() == null) {
            result.addMessages("ReactionType CANNOT BE NULL", ResultType.INVALID);
        }

        if (reviewReaction.getReviewId() <= 0) {
            result.addMessages("Valid User ID is REQUIRED", ResultType.INVALID);
        } else if (reviewRepository.findById(reviewReaction.getReviewId()) == null) {
            result.addMessages("User does not exist", ResultType.INVALID);
        }

        if (reviewReaction.getUserId() <= 0) {
            result.addMessages("Valid User ID is REQUIRED", ResultType.INVALID);
        } else if (appUserRepository.findById(reviewReaction.getUserId()) == null) {
            result.addMessages("User does not exist", ResultType.INVALID);
        }

        return result;
    }

}
