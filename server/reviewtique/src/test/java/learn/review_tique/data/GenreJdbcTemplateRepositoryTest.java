package learn.review_tique.data;

import learn.review_tique.models.Developer;
import learn.review_tique.models.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GenreJdbcTemplateRepositoryTest {

    @Autowired
    GenreJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Genre> genres = repository.findAll();

        assertEquals(11, genres.size());
    }

    @Test
    void shouldFindById() {
        Genre genre = repository.findById(1);

        assertEquals("Action", genre.getGenreName());
    }

    @Test
    void shouldFindByName() {
        List<Genre> genres = repository.searchByName("multiplooyer");

        assertEquals(1, genres.size());
        assertEquals("Multiplayer", genres.get(0).getGenreName());
    }

    @Test
    void shouldAdd() {
        Genre genre = new Genre();
        genre.setGenreName("Racing");

        Genre actual = repository.add(genre);

        assertEquals(actual, genre);
    }

    @Test
    void shouldUpdate() {
        Genre genre = new Genre();
        genre.setGenreId(2);
        genre.setGenreName("Racing");

        assertTrue(repository.update(genre));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(10));
    }
}
