package learn.review_tique.domain;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class IGDBDataImporter {

    private final RestTemplate restTemplate;
    private final GameService gameService;
    private final GenreService genreService;
    private final PlatformService platformService;
    private final DeveloperService developerService;
    private Map<Integer, Object> platforms;
    private Map<Integer, Object> genres;
    private Map<Integer, Object> companies;
    private Map<Integer, Map<String, Object>> involvedCompanies;

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
    }

    // before start

    // preload all genres into Map<Integer, Object>
    // preload involved_companies into Map<Integer, Map<String, Object>>
    // preload companies into Map<Integer, Object>
    // preload covers into Map<Integer, String>

    // preload all platforms into Map<Integer, Object>
    public Map<Integer, String> preloadPlatforms(String apiUrl) {


        Map<Integer, String> platforms = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", "hzle9t1ozr3ttnlzw17xx6zodx0csj");
        headers.set("Authorization", "Bearer rs09iax1hwcyrho9diuxfurw12n4hy");
        headers.set("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("fields id, name; limit 500;", headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
        System.out.println(response.getStatusCode());
        if(response.getStatusCode() == HttpStatus.OK) {

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                for( JsonNode node : rootNode) {
                    int id = node.get("id").asInt();
                    String name = node.get("name").asText();
                    platforms.put(id, name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            System.out.println("Failed to fetch platforms, Status Code:" + response.getStatusCode());
        }

        return platforms;
    }

    public void preloadGenres(String apiUrl) {

    }

    public void preloadCompanies(String apiUrl) {

    }

    public void preloadInvolvedCompanies(String apiUrl) {

    }




    // PLATFORMS

    // using preloaded platforms
    // Map them to Platform Objects
    // Add each to the database

    // GENRES

    // using preloaded genres
    // Map them to Genre Objects
    // Add each to the database



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




}
