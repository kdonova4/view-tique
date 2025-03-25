package learn.review_tique.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IGDBDataImporterTest {

    @Autowired
    IGDBDataImporter importer;


    @Test
    void shouldPopulate500Platforms() {
        assertEquals(219, importer.preloadPlatforms("https://api.igdb.com/v4/platforms").size());
    }
}
