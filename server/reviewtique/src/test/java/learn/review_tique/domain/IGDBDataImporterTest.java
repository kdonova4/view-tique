package learn.review_tique.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IGDBDataImporterTest {

    @Autowired
    IGDBDataImporter importer;


    @Test
    void shouldPopulate219Platforms() {
        Map<Integer, String> platforms = importer.preloadPlatforms("https://api.igdb.com/v4/platforms");
        assertEquals(219, platforms.size());
        assertEquals("Xbox 360", platforms.get(12));
    }

    @Test
    void shouldPopulate23Genres() {
        Map<Integer, String> genres = importer.preloadGenres("https://api.igdb.com/v4/genres");
        assertEquals(23, genres.size());
    }


    @Test
    void shouldPopulate58712Companies() {
        Map<Integer, String> companies = importer.preloadCompanies("https://api.igdb.com/v4/companies");
        assertEquals(58712, companies.size());

        assertEquals("Nitrome", companies.get(15443));
    }
}
