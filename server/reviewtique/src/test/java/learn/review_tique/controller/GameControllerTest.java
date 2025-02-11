package learn.review_tique.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.Developer;
import learn.review_tique.models.Game;
import learn.review_tique.models.Review;
import learn.review_tique.security.JwtConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @MockBean
    GameRepository repository;

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

        AppUser appUser = new AppUser(1, "admin_user", "85c*98Kd", false,
                List.of("ADMIN"));

        when(appUserRepository.findByUsername("admin_user")).thenReturn(appUser);

        token = jwtConverter.getTokenFromUser(appUser);
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    @Test
    void addShouldReturn400WhenEmpty() throws Exception {

        var request = post("/v1/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, null, "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        String gameJson = jsonMapper.writeValueAsString(game);

        var request = post("/v1/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(gameJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn415WhenMultipart() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, null, "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);
        String gameJson = jsonMapper.writeValueAsString(game);
        var request = post("/v1/api/games")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)
                .content(gameJson);

        mockMvc.perform(request)
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void addShouldReturn201() throws Exception {

        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(0, "null", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);
        Game expected = new Game(1, "null", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(repository.add(any())).thenReturn(expected);

        String reviewJson = jsonMapper.writeValueAsString(game);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        var request = post("/v1/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(reviewJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void updateShouldReturn204NoContent() throws Exception {

        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "null", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);


        when(repository.update(any())).thenReturn(true);

        String gameJson = jsonMapper.writeValueAsString(game);

        var request = put("/v1/api/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(gameJson);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void updateShouldReturn400WhenInvalid() throws Exception {

        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, null, "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);


        when(repository.update(any())).thenReturn(true);

        String gameJson = jsonMapper.writeValueAsString(game);

        var request = put("/v1/api/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(gameJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateShouldReturn409WhenIdsDoNotMatch() throws Exception {

        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "null", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        String gameJson = jsonMapper.writeValueAsString(game);

        var request = put("/v1/api/games/5")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(gameJson);

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    void deleteShouldReturn204NoContent() throws Exception {


        when(repository.deleteById(1)).thenReturn(true);


        var request = delete("/v1/api/games/1")
                .header("Authorization", "Bearer " + token);

        // Assert: Expecting 204 No Content as response
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404NotFoundWhenMissing() throws Exception {


        when(repository.deleteById(1)).thenReturn(false);


        var request = delete("/v1/api/games/1")
                .header("Authorization", "Bearer " + token);

        // Assert: Expecting 404 Not Found as response
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
