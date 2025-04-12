package learn.review_tique.data;

import learn.review_tique.models.Developer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
public class DeveloperJdbcTemplateRepositoryTest {

    @Autowired
    DeveloperJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Developer> developers = repository.findAll();

        assertEquals(13, developers.size());
    }

    @Test
    void shouldFindById() {
        Developer developer = repository.findById(1);

        assertEquals("Bungie", developer.getDeveloperName());
    }

    @Test
    void shouldAdd() {
        Developer developer = new Developer();
        developer.setDeveloperName("Rocksteady");

        Developer actual = repository.add(developer);

        assertEquals(developer, actual);
    }

    @Test
    void shouldUpdate() {
        Developer developer = new Developer();
        developer.setDeveloperId(7);
        developer.setDeveloperName("Obsidian");

        assertTrue(repository.update(developer));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(1));
    }
}
