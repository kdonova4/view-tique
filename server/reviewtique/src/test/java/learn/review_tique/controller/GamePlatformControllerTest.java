package learn.review_tique.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.GameGenreRepository;
import learn.review_tique.data.GamePlatformRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.models.*;
import learn.review_tique.security.JwtConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GamePlatformControllerTest {

    @MockBean
    GamePlatformRepository repository;

    @MockBean
    GameRepository gameRepository;

    @MockBean
    AppUserRepository appUserRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtConverter jwtConverter;

    String token;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @BeforeEach
    void setup() {

        AppUser appUser = new AppUser(1, "test_user1", "85c*98Kd", false,
                List.of("USER"));

        when(appUserRepository.findByUsername("test_user1")).thenReturn(appUser);

        token = jwtConverter.getTokenFromUser(appUser);
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    @Test
    void addShouldReturn400WhenEmpty() throws Exception {

        var request = post("/api/v1/game/platform")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        GamePlatform gamePlatform = new GamePlatform();

        String gamePlatformJson = jsonMapper.writeValueAsString(gamePlatform);

        var request = post("/api/v1/game/platform")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(gamePlatformJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn415WhenMultipart() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        GamePlatform gamePlatform = new GamePlatform();
        String gamePlatformJson = jsonMapper.writeValueAsString(gamePlatform);

        var request = post("/api/v1/game/platform")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)
                .content(gamePlatformJson);

        mockMvc.perform(request)
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void addShouldReturn201() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        GamePlatform gamePlatform = new GamePlatform(1, new Platform(40, "Test"));
        GamePlatform gamePlatformDummy = new GamePlatform(2, new Platform(1, "Dummy"));
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, null, "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);






        when(repository.findByGameId(1)).thenReturn(List.of(gamePlatformDummy));
        when(gameRepository.findById(1)).thenReturn(game);
        String gamePlatformJson = jsonMapper.writeValueAsString(gamePlatform);
        when(repository.add(any())).thenReturn(true);


        var request = post("/api/v1/game/platform")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(gamePlatformJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    void deleteShouldReturn204NoContent() throws Exception {


        when(repository.deleteById(1, 1)).thenReturn(true);


        var request = delete("/api/v1/game/platform/1/1")
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404NotFoundWhenMissing() throws Exception {
        when(repository.deleteById(1, 1)).thenReturn(false);

        var request = delete("/api/v1/game/platform/1/1")
                .header("Authorization", "Bearer " + token);

        // Assert: Expecting 404 Not found as response
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}