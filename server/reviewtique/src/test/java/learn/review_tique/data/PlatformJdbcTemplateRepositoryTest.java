package learn.review_tique.data;

import learn.review_tique.models.Developer;
import learn.review_tique.models.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlatformJdbcTemplateRepositoryTest {

    @Autowired
    PlatformJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Platform> platforms = repository.findAll();

        assertEquals(11, platforms.size());
    }

    @Test
    void shouldFindById() {
        Platform platform = repository.findById(1);

        assertEquals("PC", platform.getPlatformName());
    }

    @Test
    void shouldFindByName() {
        List<Platform> platforms = repository.searchByName("playstatioon");

        assertEquals(3, platforms.size());
    }

    @Test
    void shouldAdd() {
        Platform platform = new Platform();

        platform.setPlatformName("Wii");

        Platform actual = repository.add(platform);

        assertEquals(actual, platform);
    }

    @Test
    void shouldUpdate() {
        Platform platform = new Platform();
        platform.setPlatformId(11);
        platform.setPlatformName("Nintendo 64");

        assertTrue(repository.update(platform));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(5));
    }
}
