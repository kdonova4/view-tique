package learn.review_tique.domain;

import learn.review_tique.data.DeveloperRepository;
import learn.review_tique.models.Developer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class DeveloperServiceTest {

    @Autowired
    DeveloperService service;

    @MockBean
    DeveloperRepository developerRepository;

    @Test
    void shouldFindAll() {
        when(developerRepository.findAll()).thenReturn(
                List.of(
                        new Developer(1, "Bungie")
                )
        );

        List<Developer> actual = service.findAll();

        assertEquals(1, actual.size());
        verify(developerRepository).findAll();
    }

    @Test
    void shouldFindById() {
        Developer developer = new Developer(1, "Bungie");
        when(developerRepository.findById(1)).thenReturn(developer);
        Developer actual = service.findById(1);
        assertEquals(developer, actual);
    }

    @Test
    void shouldFindByName() {
        Developer developer = new Developer(1, "Bungie");
        when(developerRepository.searchByName("bungiee")).thenReturn(
                List.of(
                        developer
                )
        );
        List<Developer> actual = service.searchByName("bungiee");
        assertEquals(developer, actual.get(0));
        verify(developerRepository).searchByName("bungiee");
    }

    @Test
    void shouldAdd() {
        Developer developer = new Developer(0, "Bungie");
        Developer mockOut = new Developer(1, "Bungie");

        when(developerRepository.add(developer)).thenReturn(mockOut);
        Result<Developer> actual = service.add(developer);

        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());

    }

    @Test
    void shouldNotAddInvalid() {
        Developer developer = new Developer(1, "Bungie");

        Result<Developer> actual = service.add(developer);
        assertEquals(ResultType.INVALID, actual.getType());

        developer.setDeveloperId(0);
        developer.setDeveloperName("");
        actual = service.add(developer);
        assertEquals(ResultType.INVALID, actual.getType());

        developer.setDeveloperName(null);
        actual = service.add(developer);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Developer developer = new Developer(1, "Bungie");

        when(developerRepository.update(developer)).thenReturn(true);
        Result<Developer> actual = service.update(developer);
        assertEquals(ResultType.SUCCESS, actual.getType());

    }

    @Test
    void shouldNotUpdateMissing() {
        Developer developer = new Developer(231, "Bungie");

        when(developerRepository.update(developer)).thenReturn(false);

        Result<Developer> actual = service.update(developer);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldDelete() {
        when(developerRepository.deleteById(1)).thenReturn(true);
        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissing() {
        when(developerRepository.deleteById(1)).thenReturn(false);
        assertFalse(service.deleteById(1));
    }
}
