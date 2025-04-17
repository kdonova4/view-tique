package learn.review_tique.domain;

import learn.review_tique.data.GenreRepository;
import learn.review_tique.models.Developer;
import learn.review_tique.models.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class GenreServiceTest {

    @Autowired
    GenreService service;

    @MockBean
    GenreRepository genreRepository;

    @Test
    void shouldFindAll() {
        when(genreRepository.findAll()).thenReturn(
                List.of(
                        new Genre(1, "Action")
                )
        );

        List<Genre> actual = service.findAll();
        assertEquals(1, actual.size());
        verify(genreRepository).findAll();
    }

    @Test
    void shouldFindById() {
        Genre genre = new Genre(1, "Action");
        when(genreRepository.findById(1)).thenReturn(genre);
        Genre actual = service.findById(1);
        assertEquals(genre, actual);
    }

    @Test
    void shouldFindByName() {
        Genre genre = new Genre(1, "Action");
        when(genreRepository.searchByName("actioon")).thenReturn(
                List.of(
                        genre
                )
        );
        List<Genre> actual = service.searchByName("actioon");
        assertEquals(genre, actual.get(0));
        verify(genreRepository).searchByName("actioon");
    }

    @Test
    void shouldAdd() {
        Genre genre = new Genre(0, "Action");
        Genre mockOut = new Genre(1, "Action");

        when(genreRepository.add(genre)).thenReturn(mockOut);
        Result<Genre> actual = service.add(genre);

        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddInvalid() {
        Genre genre = new Genre(1, "Action");

        Result<Genre> actual = service.add(genre);
        assertEquals(ResultType.INVALID, actual.getType());

        genre.setGenreId(0);
        genre.setGenreName("");
        actual = service.add(genre);
        assertEquals(ResultType.INVALID, actual.getType());

        genre.setGenreName(null);
        actual = service.add(genre);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Genre genre = new Genre(1, "Action");

        when(genreRepository.update(genre)).thenReturn(true);
        Result<Genre> actual = service.update(genre);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Genre genre = new Genre(231, "Action");

        when(genreRepository.update(genre)).thenReturn(false);

        Result<Genre> actual = service.update(genre);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldDelete() {
        when(genreRepository.deleteById(1)).thenReturn(true);
        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissing() {
        when(genreRepository.deleteById(1)).thenReturn(false);
        assertFalse(service.deleteById(1));
    }
}
