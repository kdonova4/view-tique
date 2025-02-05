package learn.review_tique.domain;

import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.data.ReviewRepository;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.Developer;
import learn.review_tique.models.Game;
import learn.review_tique.models.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class ReviewServiceTest {

    @Autowired
    ReviewService service;

    @MockBean
    ReviewRepository reviewRepository;

    @MockBean
    AppUserRepository appUserRepository;

    @MockBean
    GameRepository gameRepository;

    @Test
    void shouldFindAll()
    {
        when(reviewRepository.findAll()).thenReturn(
                List.of(
                        new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1)
                )
        );

        List<Review> actual = service.findAll();

        assertEquals(1, actual.size());
        verify(reviewRepository).findAll();
    }

    @Test
    void shouldFindById() {
        Review expected = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        when(reviewRepository.findById(1)).thenReturn(expected);
        Review actual = service.findById(1);
        assertEquals(expected, actual);
        verify(reviewRepository).findById(1);
    }

    @Test
    void shouldFindByUserId() {
        when(reviewRepository.findByUserId(1)).thenReturn(
                List.of(
                        new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1)
                )
        );

        List<Review> actual = service.findByUserId(1);
        assertEquals(1, actual.size());
        verify(reviewRepository).findByUserId(1);
    }

    @Test
    void shouldFindByGameId() {
        when(reviewRepository.findByGameId(1)).thenReturn(
                List.of(
                        new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1)
                )
        );

        List<Review> actual = service.findByGameId(1);
        assertEquals(1, actual.size());
        verify(reviewRepository).findByGameId(1);
    }

    @Test
    void shouldAdd() {
        Review review = new Review(0, 0.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        Review mockOut = new Review(1, 0.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        AppUser user = new AppUser(1, "test_user1", "$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y", false, List.of("user"));
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(gameRepository.findById(1)).thenReturn(game);
        when(appUserRepository.findById(1)).thenReturn(user);
        when(reviewRepository.add(review)).thenReturn(mockOut);

        Result<Review> actual = service.add(review);
        System.out.println(actual.getMessages());
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
        assertEquals(11, game.getUserReviewCount());
        assertEquals(game.getAvgUserScore(), 7.7);
    }

    @Test
    void shouldNotAddInvalid() {
        Review review = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);

        Result<Review> actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setReviewId(0);
        review.setScore(-8.0);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setScore(1.0);
        review.setTimestamp(null);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setTimestamp(new Timestamp(System.currentTimeMillis()));
        review.setReviewBody("");
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setReviewBody(null);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setReviewBody("TEST");
        review.setLikes(-1);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setLikes(1);
        review.setDislikes(-1);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setDislikes(1);
        review.setGameId(-1);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setGameId(1);
        actual = service.add(review);
        System.out.println(actual.getMessages());
        assertEquals(ResultType.INVALID, actual.getType());

        review.setGameId(1);
        when(gameRepository.findById(1)).thenReturn(new Game());
        actual = service.add(review);
        System.out.println(actual.getMessages());
        assertEquals(ResultType.INVALID, actual.getType());

    }

    @Test
    void shouldUpdate() {
        Review review = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        Review oldReview = new Review(1, 7.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        AppUser user = new AppUser(1, "test_user1", "$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y", false, List.of("user"));
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(gameRepository.findById(1)).thenReturn(game);
        when(appUserRepository.findById(1)).thenReturn(user);
        when(reviewRepository.findById(1)).thenReturn(oldReview);
        when(reviewRepository.update(review)).thenReturn(true);
        Result<Review> actual = service.update(review);
        assertEquals(game.getUserReviewCount(), 10);
        assertEquals(game.getAvgUserScore(), 8.6);
    }

    @Test
    void shouldNotUpdateMissing() {
        Review review = new Review(1243, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        AppUser user = new AppUser(1, "test_user1", "$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y", false, List.of("user"));
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 150, 30, developer);

        when(gameRepository.findById(1)).thenReturn(game);
        when(appUserRepository.findById(1)).thenReturn(user);
        when(reviewRepository.findById(1243)).thenReturn(null);
        when(reviewRepository.update(review)).thenReturn(false);
        Result<Review> actual = service.update(review);
        System.out.println(actual.getMessages());
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateInvalid() {
        Review review = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);


        review.setScore(-8.0);
        Result<Review> actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setScore(1.0);
        review.setTimestamp(null);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setTimestamp(new Timestamp(System.currentTimeMillis()));
        review.setReviewBody("");
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setReviewBody(null);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setReviewBody("TEST");
        review.setLikes(-1);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setLikes(1);
        review.setDislikes(-1);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setDislikes(1);
        review.setGameId(-1);
        actual = service.add(review);
        assertEquals(ResultType.INVALID, actual.getType());

        review.setGameId(1);
        actual = service.add(review);
        System.out.println(actual.getMessages());
        assertEquals(ResultType.INVALID, actual.getType());

        review.setGameId(1);
        when(gameRepository.findById(1)).thenReturn(new Game());
        actual = service.add(review);
        System.out.println(actual.getMessages());
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotDeleteMissing() {
        Result<Review> result = service.deleteById(1000);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("not found"));
    }

    @Test
    void shouldDelete() {
        Review review = new Review(1, 9.9, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        AppUser user = new AppUser(1, "test_user1", "$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y", false, List.of("user"));
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 5, 30, developer);

        when(gameRepository.findById(1)).thenReturn(game);
        when(appUserRepository.findById(1)).thenReturn(user);
        when(reviewRepository.findById(1)).thenReturn(review);
        when(reviewRepository.deleteById(1)).thenReturn(true);
        Result<Review> result = service.deleteById(1);
        System.out.println(result.getMessages());
        assertTrue(result.isSuccess());
        assertEquals(game.getUserReviewCount(), 4);
        assertEquals(game.getAvgUserScore(), 8.2);
        verify(reviewRepository).deleteById(any(int.class));
    }
}
