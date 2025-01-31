package learn.review_tique.data;

import learn.review_tique.models.Wishlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WishlistJdbcTemplateRepositoryTest {

    @Autowired
    WishlistJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByUserId() {
        List<Wishlist> wishlist = repository.findByUserId(1);

        assertEquals(2, wishlist.size());
    }

    @Test
    void shouldAdd() {
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(2);
        wishlist.setGameId(2);

        Wishlist actual = repository.add(wishlist);

        assertEquals(actual, wishlist);
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(3));
    }
}
