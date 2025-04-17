package learn.review_tique.domain;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import learn.review_tique.models.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class IGDBDataImporter  implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final GameService gameService;
    private final GenreService genreService;
    private final PlatformService platformService;
    private final DeveloperService developerService;
    private Map<Integer, String> platforms;
    private Map<Integer, String> genres;
    private Map<Integer, String> companies;
    private Map<Integer, String> covers;
    private Map<Integer, Pair<Integer, Boolean>> involvedCompanies;
    public int errorCount = 0;
    public int gameCount = 0;

    public IGDBDataImporter(RestTemplate restTemplate,
                            GameService gameService,
                            GenreService genreService,
                            PlatformService platformService,
                            DeveloperService developerService) {
        this.restTemplate = restTemplate;
        this.gameService = gameService;
        this.genreService = genreService;
        this.platformService = platformService;
        this.developerService = developerService;
        platforms = new HashMap<>();
        genres = new HashMap<>();
        companies = new HashMap<>();
        covers = new HashMap<>();
        involvedCompanies = new HashMap<>();
    }

    @Override
    public void run(String... args) throws Exception {
//        System.out.println("Running IGDB data import...");
//        populate();
//        System.out.println("IGDB data import complete.");
    }

    public void populate() {
        Map<Integer, String> genres = preloadGenres("https://api.igdb.com/v4/genres");
        System.out.println("Finished preloading genres");

        Map<Integer, String> platforms = preloadPlatforms("https://api.igdb.com/v4/platforms");
        System.out.println("Finished preloading platforms");

        preloadCompanies("https://api.igdb.com/v4/companies");
        System.out.println("Finished preloading companies");

        preLoadCovers("https://api.igdb.com/v4/covers");
        System.out.println("Finished preloading covers");

        preloadInvolvedCompanies("https://api.igdb.com/v4/involved_companies");
        System.out.println("Finished preloading involved companies");

        List<Genre> newGenres = transformGenres(genres);
        System.out.println("Finished transforming genres");

        List<Platform> newPlatforms = transformPlatforms(platforms);
        System.out.println("Finished transforming platforms");

        addGenres(newGenres);
        System.out.println("Added genres");

        addPlatforms(newPlatforms);
        System.out.println("Added platforms");

        loadAndAddGames("https://api.igdb.com/v4/games");
        System.out.println("=== FINISHED ===");
    }

    // preload all platforms into Map<Integer, Object>
    public Map<Integer, String> preloadPlatforms(String apiUrl) {

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, createRequest("fields id, name; limit 500;"), String.class);
        System.out.println(response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                for (JsonNode node : rootNode) {
                    int id = node.get("id").asInt();
                    String name = node.get("name").asText();
                    platforms.put(id, name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Failed to fetch platforms, Status Code:" + response.getStatusCode());
        }

        return platforms;
    }

    public Map<Integer, String> preloadGenres(String apiUrl) {

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, createRequest("fields id, name; limit 500;"), String.class);
        System.out.println(response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                for (JsonNode node : rootNode) {
                    int id = node.get("id").asInt();
                    String name = node.get("name").asText();
                    genres.put(id, name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Failed to fetch genres, Status Code:" + response.getStatusCode());
        }

        return genres;
    }

    public Map<Integer, String> preloadCompanies(String apiUrl) {
        int offset = 0;
        int limit = 500;
        boolean fetching = true;
        int processed = 0;

        while (fetching) {

            try {
                TimeUnit.MILLISECONDS.sleep(250); // Ensures you stay under 4 requests/sec
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


            ResponseEntity<String> response = restTemplate.exchange(apiUrl,
                    HttpMethod.POST,
                    createRequest("fields id, name; limit 500; offset " + offset + ";"),
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.getBody());

                    if (rootNode.size() != limit)
                        fetching = false;
                    else
                        offset += limit;

                    for (JsonNode node : rootNode) {
                        int id = node.get("id").asInt();
                        String name = node.get("name").asText();
                        companies.put(id, name);
                        processed++;
                    }


                    System.out.print("\rProcessed " + processed + " companies");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Failed to fetch companies, Status Code:" + response.getStatusCode());
                fetching = false;
            }

        }

        return companies;
    }

    public Map<Integer, String> preLoadCovers(String apiUrl) {
        int offset = 0;
        int limit = 500;
        boolean fetching = true;
        int processed = 0;

        while (fetching) {

            try {
                TimeUnit.MILLISECONDS.sleep(250); // Ensures you stay under 4 requests/sec
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


            ResponseEntity<String> response = restTemplate.exchange(apiUrl,
                    HttpMethod.POST,
                    createRequest("fields id, url; limit 500; offset " + offset + ";"),
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.getBody());

                    if (rootNode.size() != limit)
                        fetching = false;
                    else
                        offset += limit;

                    for (JsonNode node : rootNode) {
                        int id = node.get("id").asInt();
                        String url = node.get("url").asText().replace("t_thumb", "t_cover_big");
                        covers.put(id, url);
                        processed++;
                    }

                    System.out.print("\rProcessed " + processed + " covers");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Failed to fetch covers, Status Code:" + response.getStatusCode());
                fetching = false;
            }

        }

        return covers;
    }

    public Map<Integer, Pair<Integer, Boolean>> preloadInvolvedCompanies(String apiUrl) {
        int limit = 500;
        int offset = 0;
        int processed = 0;

        boolean fetching  = true;

        while(fetching) {

            try {
                TimeUnit.MILLISECONDS.sleep(250); // Ensures you stay under 4 requests/sec
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            ResponseEntity<String> response = restTemplate.exchange(apiUrl,
                    HttpMethod.POST,
                    createRequest("fields id, company, developer; limit 500; offset " + offset + ";"),
                    String.class);

            if(response.getStatusCode() == HttpStatus.OK) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.getBody());

                    if (rootNode.size() != limit)
                        fetching = false;
                    else
                        offset += limit;

                    for (JsonNode node : rootNode) {
                        int id = node.get("id").asInt();
                        int companyId = node.get("company").asInt();
                        boolean isDeveloper = node.get("developer").asBoolean();
                        Pair<Integer, Boolean> companyInfo = Pair.of(companyId, isDeveloper);
                        involvedCompanies.put(id, companyInfo);
                        processed++;
                    }

                    System.out.print("\rProcessed " + processed + " involved companies");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to fetch Involved Companies, Status Code:" + response.getStatusCode());
                fetching = false;
            }

        }

        return involvedCompanies;
    }

    public List<Platform> transformPlatforms(Map<Integer, String> platforms) {
        List<Platform> newPlatforms = new ArrayList<>();
        for (String platform : platforms.values()) {
            newPlatforms.add(new Platform(0, platform));
        }

        return newPlatforms;
    }

    public List<Genre> transformGenres(Map<Integer, String> genres) {
        List<Genre> newGenres = new ArrayList<>();
        for (String genre : genres.values()) {
            newGenres.add(new Genre(0, genre));
        }

        return newGenres;
    }


    public void loadAndAddGames(String apiUrl) {
        int limit = 500;
        int offset = 0;
        boolean fetching = true;
        int processed = 0;


        while(fetching) {

            ResponseEntity<String> response = restTemplate.exchange(apiUrl,
                    HttpMethod.POST,
                    createRequest("fields cover, " +
                            "first_release_date, genres, name, " +
                            "platforms, summary, involved_companies; " +
                            "limit 500; where category = (0, 2, 4, 8, 9, 11) & cover != null & involved_companies != null" +
                            " & first_release_date != null & summary != null; offset " + offset + ";"),
                    String.class);

            if(response.getStatusCode() == HttpStatus.OK) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.getBody());

                    if (rootNode.size() != limit)
                        fetching = false;
                    else
                        offset += limit;

                    String title;
                    String description;
                    String coverUrl;
                    long timestamp;
                    LocalDate releaseDate;
                    Instant instant;

                    for(JsonNode node : rootNode) {

                        List<Genre> gameGenres = new ArrayList<>();
                        List<Platform> gamePlatforms = new ArrayList<>();
                        Developer developer = new Developer(0, null);
                        JsonNode genreArray = node.get("genres");
                        JsonNode platformsArray = node.get("platforms");
                        JsonNode involvedCompaniesArray = node.get("involved_companies");

                        if(genreArray != null && genreArray.isArray()) {
                            for(JsonNode genre : genreArray) {
                                int genreId = genre.asInt();
                                String genreName = genres.get(genreId);
                                Genre g = genreService.findByName(genreName);

                                if(g != null)
                                    gameGenres.add(g);
                            }
                        }

                        if(platformsArray != null && platformsArray.isArray()) {
                            for(JsonNode platform : platformsArray) {
                                int platformId = platform.asInt();
                                String platformName = platforms.get(platformId);
                                Platform p = platformService.findByName(platformName);

                                if(p != null)
                                    gamePlatforms.add(p);
                            }
                        }

                        if(involvedCompaniesArray != null && involvedCompaniesArray.isArray()) {
                            for(JsonNode involvedCompany : involvedCompaniesArray) {
                                int involvedCompanyId = involvedCompany.asInt();
                                Pair<Integer, Boolean> p = involvedCompanies.get(involvedCompanyId);

                                if(p.getRight()) {
                                    String developerName = companies.get(p.getLeft());
                                    developer.setDeveloperName(developerName);
                                    break;
                                }
                            }
                        }

                        // skip game if no developer
                        if(developer.getDeveloperName() == null)
                            continue;

                        // get release date
                        timestamp = node.get("first_release_date").asLong();
                        instant = Instant.ofEpochSecond(timestamp);
                        releaseDate = instant.atZone(ZoneOffset.UTC).toLocalDate();

                        if(releaseDate.isAfter(LocalDate.now()))
                            continue;

                        //get title
                        title = node.get("name").asText();
                        //get description
                        description = node.get("summary").asText();
                        //get cover url
                        int coverId = node.get("cover").asInt();
                        coverUrl = covers.get(coverId);

                        Game game = new Game(0, title, description, releaseDate, 0.0, 0.0, 0, 0, null);
                        game.setCover(coverUrl);

                        addGameAndRelationships(game, gameGenres, gamePlatforms, developer);
                        processed++;
                    }
                    System.out.println("\rProcessed " + processed + " games");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to fetch Games, Status Code:" + response.getStatusCode());
                fetching = false;
            }
        }
    }

    private void addGameAndRelationships(Game game, List<Genre> thisGenres, List<Platform> thisPlatforms, Developer developer) {
        int gameId = 0;
        //check if developer name is null
        if(developer.getDeveloperName() == null)
            return;

        System.out.println("==================================");
        Developer existingDev = developerService.findByName(developer.getDeveloperName());
        if(existingDev != null) {
            game.setDeveloper(existingDev);
        } else {
            Result<Developer> devResult = developerService.add(developer);
            if(devResult.isSuccess()) {
                game.setDeveloper(devResult.getPayload());
                System.out.println("| Added Developer " + devResult.getPayload().getDeveloperId());
            } else
                return;
        }


        Result<Game> gameResult = gameService.add(game);
        if(gameResult.isSuccess()) {
            gameId = gameResult.getPayload().getGameId();
            System.out.println("| Added Game " + gameId);
            gameCount++;
        }
        else {
            errorCount++;
            System.out.println("***************************************************************************");
            System.out.println(game.getTitle() + " FAILED <--------------------------------------");
            System.out.println(gameResult.getMessages());
            System.out.println("***************************************************************************");
            return;
        }

        for(Platform platform : thisPlatforms) {
            gameService.add(new GamePlatform(gameId, platform));
            System.out.println("| Added GamePlatform [" + gameId + "," + platform.getPlatformId() + "]");
        }
        for(Genre genre : thisGenres) {
            gameService.add(new GameGenre(gameId, genre));
            System.out.println("| Added GameGenre [" + gameId + "," + genre.getGenreId() + "]");
        }

        System.out.println("==================================\n");

    }

    public void addPlatforms(List<Platform> platforms) {
        for(Platform platform : platforms) {
            if(platformService.findByName(platform.getPlatformName()) == null)
                platformService.add(platform);
        }
    }

    public void addGenres(List<Genre> genres) {
        for(Genre genre : genres) {
            if(genreService.findByName(genre.getGenreName()) == null)
                genreService.add(genre);
        }
    }

    private HttpEntity<String> createRequest(String body) {
        String bearerToken = System.getenv("BEARER_TOKEN");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", "hzle9t1ozr3ttnlzw17xx6zodx0csj");
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.set("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(body, headers);
    }


}
