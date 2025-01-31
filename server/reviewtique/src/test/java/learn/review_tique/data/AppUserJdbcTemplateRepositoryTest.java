package learn.review_tique.data;

import learn.review_tique.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    AppUserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByUsername() {
        AppUser user = repository.findByUsername("test_user1");

        assertNotNull(user);
    }

    @Test
    void shouldFindById() {
        AppUser user = repository.findById(1);

        assertEquals("test_user1", user.getUsername());
    }

    @Test
    void shouldCreate() {
        AppUser user = new AppUser(0, "kdonova4", "85c*98Kd", false, new ArrayList<String>());

        AppUser actual = repository.create(user);

        assertEquals(user, actual);
    }

    @Test
    void shouldUpdate() {
        AppUser user = new AppUser(5, "midway2223", "85c*98Kd", false, new ArrayList<String>());

        repository.update(user);

        assertEquals("midway2223", repository.findById(5).getUsername());
    }

}
