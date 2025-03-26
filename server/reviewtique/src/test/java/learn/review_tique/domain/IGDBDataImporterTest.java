package learn.review_tique.domain;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
        assertEquals("Shooter", genres.get(5));
    }


    @Test
    void shouldPopulate58712Companies() {
        Map<Integer, String> companies = importer.preloadCompanies("https://api.igdb.com/v4/companies");

        assertEquals("Nitrome", companies.get(15443));
    }

    @Test
    void shouldPopulate246660Covers() {
        Map<Integer, String> covers = importer.preLoadCovers("https://api.igdb.com/v4/covers");

        assertEquals("//images.igdb.com/igdb/image/upload/t_cover_big/co9kt3.jpg", covers.get(446871));
    }

    @Test
    void shouldPopulate220837InvolvedCompanies() {
        Map<Integer, Pair<Integer, Boolean>> involvedCompanies = importer.preloadInvolvedCompanies("https://api.igdb.com/v4/involved_companies");

        assertEquals(true, involvedCompanies.get(95000).getRight());
        assertEquals(14486, involvedCompanies.get(95000).getLeft());
    }
}
