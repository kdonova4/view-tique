package learn.review_tique.domain;

import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.ReviewReactionRepository;
import learn.review_tique.data.ReviewRepository;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.ReactionType;
import learn.review_tique.models.Review;
import learn.review_tique.models.ReviewReaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class ReviewReactionServiceTest {

    @Autowired
    ReviewReactionService service;

    @MockBean
    ReviewReactionRepository reactionRepository;

    @MockBean
    ReviewRepository reviewRepository;

    @MockBean
    AppUserRepository appUserRepository;

    @Test
    void shouldFindById() {
        ReviewReaction reviewReaction = new ReviewReaction(1, 1, 1, ReactionType.LIKE);
        when(reactionRepository.findById(1)).thenReturn(reviewReaction);
        ReviewReaction actual = service.findById(1);
        assertEquals(actual, reviewReaction);
    }

    @Test
    void shouldFindByReviewIdAndUserId() {
        ReviewReaction reviewReaction = new ReviewReaction(1, 1, 1, ReactionType.LIKE);
        when(reactionRepository.findByReviewIdAndUserId(1, 1)).thenReturn(reviewReaction);
        ReviewReaction actual = service.findByReviewAndUserId(1, 1);
        assertEquals(actual, reviewReaction);
    }

    @Test
    void shouldAdd() {
        ReviewReaction reviewReaction = new ReviewReaction(0, 1, 1, ReactionType.LIKE);
        ReviewReaction mockOut = new ReviewReaction(1, 1, 1, ReactionType.LIKE);
        Review review = new Review(1, 0.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        AppUser user = new AppUser(1, "test_user1", "$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y", false, List.of("user"));

        when(appUserRepository.findById(1)).thenReturn(user);
        when(reviewRepository.findById(1)).thenReturn(review);
        when(reactionRepository.add(reviewReaction)).thenReturn(mockOut);

        Result<ReviewReaction> actual = service.add(reviewReaction);
        System.out.println(actual.getMessages());
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
        assertEquals(review.getLikes(), 1);
    }

    @Test
    void shouldNotAddInvalid() {
        ReviewReaction reviewReaction = new ReviewReaction(1, 1, 1, ReactionType.LIKE);

        Result<ReviewReaction> actual = service.add(reviewReaction);
        assertEquals(ResultType.INVALID, actual.getType());

        reviewReaction.setReviewReactionId(0);
        reviewReaction.setReviewId(0);
        actual = service.add(reviewReaction);
        assertEquals(ResultType.INVALID, actual.getType());

        reviewReaction.setReviewId(1);
        reviewReaction.setUserId(0);
        actual = service.add(reviewReaction);
        assertEquals(ResultType.INVALID, actual.getType());

        reviewReaction.setUserId(1);
        reviewReaction.setReactionType(null);
        actual = service.add(reviewReaction);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldDelete() {
        ReviewReaction reviewReaction = new ReviewReaction(1, 1, 1, ReactionType.LIKE);
        Review review = new Review(1, 0.0, new Timestamp(System.currentTimeMillis()), "Test review", 1, 0, 1, 1);
        when(reactionRepository.findById(1)).thenReturn(reviewReaction);
        when(reviewRepository.findById(1)).thenReturn(review);
        when(reactionRepository.deleteById(1)).thenReturn(true);
        assertTrue(service.deleteById(1));
        assertEquals(0, review.getLikes());
    }

    @Test
    void shouldNotDeleteMissing() {
        when(reactionRepository.deleteById(1)).thenReturn(false);
        assertFalse(service.deleteById(1));
    }
}
