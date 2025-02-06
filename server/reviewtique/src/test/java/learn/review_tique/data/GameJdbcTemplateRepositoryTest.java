package learn.review_tique.data;

import learn.review_tique.models.Developer;
import learn.review_tique.models.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameJdbcTemplateRepositoryTest {

    @Autowired
    GameJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Game> games = repository.findAll();

        assertEquals(6, games.size());
    }

    @Test
    void shouldFindById() {
        Game game = repository.findById(2);

        assertEquals("Halo 2", game.getTitle());
    }

    @Test
    void shouldAdd() {
        Game game = new Game();
        game.setUserReviewCount(0);
        game.setCriticReviewCount(0);
        game.setAvgCriticScore(0);
        game.setAvgUserScore(0);
        game.setDescription("Test Description");
        game.setTitle("Test Game");
        game.setReleaseDate(LocalDate.now().minusMonths(65));

        Developer developer = new Developer();
        developer.setDeveloperId(4);
        game.setDeveloper(developer);

        Game actual = repository.add(game);

        assertEquals(actual, game);
    }

    @Test
    void shouldFindGameFromSearch() {
        List<Game> games = repository.searchGame("battle", new int[]{6, 8}, null, null);

        assertEquals(2, games.size());

        games = repository.searchGame("", null, null, 2);

        assertEquals(2, games.size());

        games = repository.searchGame("hal", new int[]{6}, new int[]{6}, 1);

        assertEquals(1, games.size());

        games = repository.searchGame("witch", null, null, null);

        assertEquals(1, games.size());

    }

    @Test
    void shouldUpdate() {
        Game game = new Game();
        game.setGameId(4);
        game.setUserReviewCount(0);
        game.setCriticReviewCount(0);
        game.setAvgCriticScore(0);
        game.setAvgUserScore(0);
        game.setDescription("Test Description");
        game.setTitle("Test Game");
        game.setReleaseDate(LocalDate.now().minusMonths(65));

        Developer developer = new Developer();
        developer.setDeveloperId(6);
        game.setDeveloper(developer);

        assertTrue(repository.update(game));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(4));
    }

}
