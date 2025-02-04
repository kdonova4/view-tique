package learn.review_tique.domain;

import learn.review_tique.data.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest()
public class GenreServiceTest {

    @Autowired
    GenreService service;

    @MockBean
    GenreRepository genreRepository;

    @Test
    void shouldFindAll() {
        
    }
}
