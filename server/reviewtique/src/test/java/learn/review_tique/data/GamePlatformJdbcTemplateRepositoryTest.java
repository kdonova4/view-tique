package learn.review_tique.data;

import learn.review_tique.models.GamePlatform;
import learn.review_tique.models.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GamePlatformJdbcTemplateRepositoryTest {

    @Autowired
    GamePlatformJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAdd() {
        Platform platform = new Platform();
        platform.setPlatformId(10);

        GamePlatform gamePlatform = new GamePlatform();
        gamePlatform.setPlatform(platform);
        gamePlatform.setGameId(5);

        assertTrue(repository.add(gamePlatform));
    }

    @Test
    void shouldFindByGameId() {
        List<GamePlatform> gamePlatforms = repository.findByGameId(1);

        assertEquals(5, gamePlatforms.size());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(2, 1));
    }
}
