package learn.review_tique.data;

import learn.review_tique.models.GameGenre;
import learn.review_tique.models.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameGenreJdbcTemplateRepositoryTest {

    @Autowired
    GameGenreJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAdd() {
        Genre genre = new Genre();
        genre.setGenreId(10);
        GameGenre gameGenre = new GameGenre();
        gameGenre.setGameId(1);
        gameGenre.setGenre(genre);

        assertTrue(repository.add(gameGenre));

    }

    @Test
    void shouldFindById() {
        List<GameGenre> gameGenres = repository.findByGameId(1);

        assertEquals(4, gameGenres.size());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(1, 2));
    }
}
