package learn.review_tique.domain;

import learn.review_tique.data.PlatformRepository;
import learn.review_tique.models.Genre;
import learn.review_tique.models.Platform;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class PlatformServiceTest {

    @Autowired
    PlatformService service;

    @MockBean
    PlatformRepository platformRepository;

    @Test
    void shouldFindAll() {
        when(platformRepository.findAll()).thenReturn(
                List.of(
                        new Platform(1, "Xbox")
                )
        );

        List<Platform> actual = service.findAll();
        assertEquals(1, actual.size());
        verify(platformRepository).findAll();
    }

    @Test
    void shouldFindById() {
        Platform platform = new Platform(1, "Xbox");
        when(platformRepository.findById(1)).thenReturn(platform);
        Platform actual = service.findById(1);
        assertEquals(platform, actual);
    }

    @Test
    void shouldFindByName() {
        Platform platform = new Platform(1, "Xbox");
        when(platformRepository.searchByName("xboox")).thenReturn(
                List.of(
                        platform
                )
        );
        List<Platform> actual = service.searchByName("xboox");
        assertEquals(platform, actual.get(0));
        verify(platformRepository).searchByName("xboox");
    }

    @Test
    void shouldAdd() {
        Platform platform = new Platform(0, "Xbox");
        Platform mockOut = new Platform(1, "Xbox");

        when(platformRepository.add(platform)).thenReturn(mockOut);
        Result<Platform> actual = service.add(platform);

        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddInvalid() {
        Platform platform = new Platform(1, "Xbox");

        Result<Platform> actual = service.add(platform);
        assertEquals(ResultType.INVALID, actual.getType());

        platform.setPlatformId(0);
        platform.setPlatformName("");
        actual = service.add(platform);
        assertEquals(ResultType.INVALID, actual.getType());

        platform.setPlatformName(null);
        actual = service.add(platform);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Platform platform = new Platform(1, "Xbox");

        when(platformRepository.update(platform)).thenReturn(true);
        Result<Platform> actual = service.update(platform);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Platform platform = new Platform(231, "Xbox");

        when(platformRepository.update(platform)).thenReturn(false);

        Result<Platform> actual = service.update(platform);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldDelete() {
        when(platformRepository.deleteById(1)).thenReturn(true);
        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissing() {
        when(platformRepository.deleteById(1)).thenReturn(false);
        assertFalse(service.deleteById(1));
    }
}
