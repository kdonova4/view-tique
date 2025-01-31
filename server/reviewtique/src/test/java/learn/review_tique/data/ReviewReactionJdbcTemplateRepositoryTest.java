package learn.review_tique.data;

import learn.review_tique.models.ReactionType;
import learn.review_tique.models.ReviewReaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class ReviewReactionJdbcTemplateRepositoryTest {

    @Autowired
    ReviewReactionJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        ReviewReaction reviewReaction = repository.findById(1);

        assertEquals(ReactionType.LIKE, reviewReaction.getReactionType());
    }

    @Test
    void shouldFindByReviewIdAndUserId() {
        ReviewReaction reviewReaction = repository.findByReviewIdAndUserId(2, 1);

        assertEquals(ReactionType.DISLIKE, reviewReaction.getReactionType());
    }

    @Test
    void shouldAdd() {
        ReviewReaction reviewReaction = new ReviewReaction();

        reviewReaction.setReactionType(ReactionType.LIKE);
        reviewReaction.setReviewId(2);
        reviewReaction.setUserId(2);

        ReviewReaction actual = repository.add(reviewReaction);

        assertEquals(reviewReaction, actual);
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(3));
    }
}
