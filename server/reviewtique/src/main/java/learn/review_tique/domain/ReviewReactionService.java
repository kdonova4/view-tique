package learn.review_tique.domain;

public class ReviewReactionService {

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

}
