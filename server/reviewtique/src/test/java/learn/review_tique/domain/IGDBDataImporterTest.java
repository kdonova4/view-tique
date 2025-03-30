package learn.review_tique.domain;

import learn.review_tique.models.Game;
import learn.review_tique.models.Genre;
import learn.review_tique.models.Platform;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IGDBDataImporterTest {

    @Autowired
    IGDBDataImporter importer;

    @Autowired
    PlatformService platformService;

    @Autowired
    GenreService genreService;

    @Autowired
    GameService gameService;

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

    @Test
    void shouldTransformAllPlatforms() {
        Map<Integer, String> platforms = importer.preloadPlatforms("https://api.igdb.com/v4/platforms");
        List<Platform> newPlatforms = importer.transformPlatforms(platforms);
        assertEquals(219, newPlatforms.size());
        assertEquals(newPlatforms.get(1).getPlatformId(), 0);
    }

    @Test
    void shouldTransformAllGenres() {
        Map<Integer, String> genres = importer.preloadGenres("https://api.igdb.com/v4/genres");
        List<Genre> newGenres = importer.transformGenres(genres);
        assertEquals(23, newGenres.size());
        assertEquals(newGenres.get(1).getGenreId(), 0);
    }

    @Test
    void shouldLoadTransformAndAddPlatforms() {
        Map<Integer, String> platforms = importer.preloadPlatforms("https://api.igdb.com/v4/platforms");
        List<Platform> newPlatforms = importer.transformPlatforms(platforms);
        importer.addPlatforms(newPlatforms);
        List<Platform> allPlatforms = platformService.findAll();

        assertEquals(219, allPlatforms.size());
    }

    @Test
    void shouldLoadTransformAndAddGenres() {
        Map<Integer, String> genres = importer.preloadGenres("https://api.igdb.com/v4/genres");
        List<Genre> newGenres = importer.transformGenres(genres);
        importer.addGenres(newGenres);
        List<Genre> allGenres = genreService.findAll();

        assertEquals(23, allGenres.size());
        assertEquals("Indie", genreService.findById(1).getGenreName());
    }

    @Test
    void shouldLoadAndTransformAndAddAllGames() {
        //set up
        Map<Integer, String> genres = importer.preloadGenres("https://api.igdb.com/v4/genres");
        System.out.println("Finished preloading genres");

        Map<Integer, String> platforms = importer.preloadPlatforms("https://api.igdb.com/v4/platforms");
        System.out.println("Finished preloading platforms");

        importer.preloadCompanies("https://api.igdb.com/v4/companies");
        System.out.println("Finished preloading companies");

        importer.preLoadCovers("https://api.igdb.com/v4/covers");
        System.out.println("Finished preloading covers");

        importer.preloadInvolvedCompanies("https://api.igdb.com/v4/involved_companies");
        System.out.println("Finished preloading involved companies");

        List<Genre> newGenres = importer.transformGenres(genres);
        System.out.println("Finished transforming genres");

        List<Platform> newPlatforms = importer.transformPlatforms(platforms);
        System.out.println("Finished transforming platforms");

        importer.addGenres(newGenres);
        System.out.println("Added genres");

        importer.addPlatforms(newPlatforms);
        System.out.println("Added platforms");

        importer.loadAndAddGames("https://api.igdb.com/v4/games");
        System.out.println("ERROR COUNT: " + importer.errorCount);
        System.out.println("GAME COUNT: " + importer.gameCount);
        List<Game> allGames = gameService.findAll();
        assertTrue(allGames.size() < 92800 && allGames.size() > 92700);
    }
}
