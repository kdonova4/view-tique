package learn.review_tique.domain;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import learn.review_tique.models.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class IGDBDataImporter {

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
                    }
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
                    }
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
                    }
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
        List<Game> games = new ArrayList<>();
        List<Genre> gameGenres = new ArrayList<>();
        List<Platform> gamePlatforms = new ArrayList<>();
        while(fetching) {
            try {
                TimeUnit.MILLISECONDS.sleep(250); // Ensures you stay under 4 requests/sec
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            ResponseEntity<String> response = restTemplate.exchange(apiUrl,
                    HttpMethod.POST,
                    createRequest("fields cover, first_release_date, genres, name, platforms, summary, involved_companies; limit 500; where category = (0, 2, 4, 8, 9) & cover != null & involved_companies != null; offset " + offset + ";"),
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
                                }
                            }
                        }

                        // get release date
                        timestamp = node.get("first_release_date").asLong();
                        instant = Instant.ofEpochSecond(timestamp);
                        releaseDate = instant.atZone(ZoneOffset.UTC).toLocalDate();

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
                    }
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

        Developer existingDev = developerService.findByName(developer.getDeveloperName());
        if(existingDev != null) {
            game.setDeveloper(existingDev);
        } else {
            Result<Developer> devResult = developerService.add(developer);
            if(devResult.isSuccess())
                game.setDeveloper(devResult.getPayload());
            else
                return;
        }

        Result<Game> gameResult = gameService.add(game);
        if(gameResult.isSuccess())
            gameId = gameResult.getPayload().getGameId();
        else {
            return;
        }

        for(Platform platform : thisPlatforms) {
            gameService.add(new GamePlatform(gameId, platform));
        }
        for(Genre genre : thisGenres) {
            gameService.add(new GameGenre(gameId, genre));
        }

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

    // GAMES

    // map IGDB game object to game model


    // GAME DEVELOPER

    // when adding games and you need to add the developer

    // you check the involved companies array form IGDB with the preloaded involved_companies map using the id

    // you then see for that company in the involved_companies table if the developer field is true if not move on

    // if it is true then get the company id and  get the developer form the companies preloaded table from IGDB  and make a developer object

    // add that to my database and then add it to the game object you made

    // add game object to database


    // GAME_PLATFORMS

    // I have already added each platform into the database

    // I will then add every main game to the database after each game is added I have to add the that game id and platform id to the gamePlatform table

    // I will take the platforms array from IGDB data and loop through each id looking through the map of pre-loaded igdb platform data I have in a map<Integer, Map<String, Object>>

    // When I find that I take the name of that platform and call service.findByName(name) on my own database to find the platform id

    // I create the GamePlatform object to add into the GamePlatform table


    // GAME_GENRES

    // I have already added each genre into the database

    // I will then add every main game to the database after each game is added I have to add the that game id and genre id to the gameGenre table

    // I will take the genres array from IGDB data and loop through each id looking through the map of pre-loaded igdb genre data I have in a map<Integer, Map<String, Object>>

    // When I find that I take the name of that platform and call service.findByName(name) on my own database to find the platform id

    // I create the GameGenre object to add into the GameGenre table

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
