package learn.review_tique.domain;

import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.data.ReviewRepository;
import learn.review_tique.models.Review;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final GameRepository gameRepository;
    private final AppUserRepository appUserRepository;
    public ReviewService(ReviewRepository repository, GameRepository gameRepository, AppUserRepository appUserRepository) {
        this.repository = repository;
        this.gameRepository = gameRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    public Review findById(int reviewId) {
        return repository.findById(reviewId);
    }

    public List<Review> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public List<Review> findByGameId(int gameId) {
        return repository.findByGameId(gameId);
    }

    /*
        Review Data
        ============
        - Review Score
            - GREATER THAN 0
            - LESS THAN OR EQUAL TO 10
        - Review Time
            - NOT NULL
            - MUST BE IN THE PAST
        - Review Body
            - NOT NULL
            - MUST BE UNDER 2,000 CHARACTERS
        - Likes
            - GREATER OR EQUAL TO 0
        - Dislikes
            - GREATER OR EQUAL TO 0
        - App User ID
            - NOT NULL
        - Game ID
            - NOT NULL
            - CANNOT APPEAR MORE THAN ONCE PER USER

        public Result<Review> add(Review review)
            // generate timestamp here
            // validate()
            // check if validate is a success, if not then return result
            // check if ID is 0 if not then add result message, and resultType nad return result
            // if still successful add, setPayload
            // call --> calculateAvgScore(review, true)
            // return result

        public Result<Review> update(Review review)
            // generate new timestamp here
            // validate()
            // check if validate() was successful, if not return result
            // check if reviewId is set, if not add result message and type and return result
            // call update and check if it is successful, if not add message that it's not found and type
            // call updateAvgScore(repository.findById(review.getUserId()))
            // return result;

        public boolean deleteById(int reviewId)
            // make sure review exists, if not return result with message and type
            // If result does exist then call
                calculateAvgScore(repository.findById(reviewId), false);
            // return result

        private Result<Review> validate(Review review)
            // initialize new result
            // check if review is null, if it is then add message to result, invalid type and return result
                    // we return result here because we don't need to run any other validation if review is null
            // check if the Review Score is GREATER THAN 0 and LESS THAN OR EQUAL TO 10
            // check if Review Time is NULL
            // check if Review Body is Blank
            // check if Likes >= 0
            // check if Dislikes >= 0
            // check if App User ID is valid
            // check if Game ID is valid
            // check if user has reviewed this game already, find reviews by userId if list contains same gameId
               then add message type and return result

        private void addDeleteAvgScore(Review review, boolean adding)
            // if adding is true
                // get userAvgScore * userReviewCount, store in variable totalScore
                // add 1 to userReviewCount
                // add review.getScore() to totalScore
                // newScore is now totalScore divided by userReviewCount
                // set avgUserScore to newScore
            // if adding is false
                // get userAvgScore * userReviewCount, store in variable totalScore
                // subtract 1 from userReviewCount
                // subtract review.getScore() from totalScore
                // newScore is now totalScore divided by userReviewCount

        private void updateAvgScore(Review review)
            // get avgUserScore * userReviewCount = totalScore
            // totalScore -= oldScore;
            // newAvg = totalScore / userReviewCount;
            // set avgUserScore to newScore

     */

    private Result<Review> validate(Review review) {
        Result<Review> result = new Result<>();

        if(review == null) {
            result.addMessages("Review CANNOT be NULL", ResultType.INVALID);
            return result;
        }

        if(review.getScore() < 0.0 || review.getScore() > 10.0) {
            result.addMessages("Review Scores MUST be between 0.0 and 10.0", ResultType.INVALID);
        }

        if(review.getTimestamp() == null) {
            result.addMessages("Review Time CANNOT be NULL", ResultType.INVALID);
        }

        if(review.getReviewBody().isBlank() || review.getReviewBody() == null) {
            result.addMessages("Review Body is REQUIRED", ResultType.INVALID);
        }

        if(review.getLikes() < 0) {
            result.addMessages("Likes must be GREATER OR EQUAL THAN 0", ResultType.INVALID);
        }

        if(review.getDislikes() < 0) {
            result.addMessages("Likes must be GREATER OR EQUAL THAN 0", ResultType.INVALID);
        }

        if(appUserRepository.findById(review.getUserId()) == null) {
            result.addMessages("Valid User ID is REQUIRED", ResultType.INVALID);
        }

        if(gameRepository.findById(review.getGameId()) == null) {
            result.addMessages("Valid Game ID is REQUIRED", ResultType.INVALID);
        }

        // check if user has already reviewed this game
        List<Review> reviews = repository.findByUserId(review.getUserId());
        for(Review r : reviews) {
            if(review.getGameId() == r.getGameId()) {
                result.addMessages("User has already reviewed this game", ResultType.INVALID);
                return result;
            }
        }

        return result;
    }

}
