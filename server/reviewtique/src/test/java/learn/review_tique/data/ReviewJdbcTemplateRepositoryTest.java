package learn.review_tique.data;

import learn.review_tique.models.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReviewJdbcTemplateRepositoryTest {

    @Autowired
    ReviewJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Review> reviews = repository.findAll();

        assertEquals(7, reviews.size());
    }

    @Test
    void shouldFindById() {
        Review review = repository.findById(1);

        assertEquals(8.5, review.getScore());
    }

    @Test
    void shouldFindByUserId() {
        List<Review> reviews = repository.findByUserId(1);

        // if we delete first its 5 if we check after adding and before delete its 7
        assertTrue(reviews.size() >= 5 && reviews.size() <= 7);
    }

    @Test
    void shouldFindByGameId() {
        List<Review> reviews = repository.findByGameId(1);

        assertEquals(2, reviews.size());
    }

    @Test
    void shouldAdd() {
        Review review = new Review();
        review.setScore(8.2);
        review.setTimestamp(new Timestamp(System.currentTimeMillis()));
        review.setReviewBody("This game is amazing!");
        review.setLikes(10);
        review.setDislikes(2);
        review.setUserId(1);
        review.setGameId(2);

        Review actual = repository.add(review);

        assertEquals(8, actual.getReviewId());
    }

    @Test
    void shouldUpdate() {
        Review review = new Review();
        review.setReviewId(2);
        review.setScore(8.1);
        review.setTimestamp(new Timestamp(System.currentTimeMillis()));
        review.setReviewBody("Great game with immersive story and excellent gameplay mechanics. Has some bugs though!");
        review.setLikes(10);
        review.setDislikes(8);
        review.setUserId(2);
        review.setGameId(2);
        assertTrue(repository.update(review));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(6));
    }
}
