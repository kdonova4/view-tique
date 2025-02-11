package learn.review_tique.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.data.ReviewRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @MockBean
    ReviewRepository repository;

    @MockBean
    AppUserRepository appUserRepository;

    @MockBean
    GameRepository gameRepository;

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
        var request = post("/v1/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();

        Review review = new Review();

        review.setUserId(1);

        String reviewJson = jsonMapper.writeValueAsString(review);

        var request = post("/v1/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(reviewJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void assShouldReturn415WhenMultiPart() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();

        Review review = new Review(0, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        String reviewJson = jsonMapper.writeValueAsString(review);

        var request = post("/v1/api/reviews")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)
                .content(reviewJson);

        mockMvc.perform(request)
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void addShouldReturn201() throws Exception {
        Review review = new Review(0, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        Review expected = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        AppUser appUser = new AppUser(1, "test_user1", "85c*98Kd", false,
                List.of("USER"));
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(gameRepository.findById(1)).thenReturn(game);
        when(repository.add(any())).thenReturn(expected);
        when(appUserRepository.findById(1)).thenReturn(appUser);
        String reviewJson = jsonMapper.writeValueAsString(review);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        var request = post("/v1/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(reviewJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void updateShouldReturn204NoContent() throws Exception {
        AppUser appUser = new AppUser(1, "test_user1", "85c*98Kd", false,
                List.of("USER"));
        Review review = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        Review oldReview = new Review(1, 7.0, new Timestamp(System.currentTimeMillis()), "Test review", 0, 0, 1, 1);
        AppUser user = new AppUser(1, "test_user1", "$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y", false, List.of("USER"));
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);


        when(gameRepository.findById(1)).thenReturn(game);
        when(appUserRepository.findById(1)).thenReturn(user);
        when(repository.findById(1)).thenReturn(oldReview);
        when(repository.update(any())).thenReturn(true);
        when(appUserRepository.findById(1)).thenReturn(appUser);

        String reviewJson = jsonMapper.writeValueAsString(review);

        var request = put("/v1/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(reviewJson);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void updateShouldReturn400WhenInvalid() throws Exception {

        Review review = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), null, 0, 0, 1, 1);

        String reviewJson = jsonMapper.writeValueAsString(review);

        var request = put("/v1/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(reviewJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
    @Test
    void updateShouldReturn409WhenIdsDoNotMatch() throws Exception {
        Review review = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), null, 0, 0, 1, 1);


        String reviewJson = jsonMapper.writeValueAsString(review);

        var request = put("/v1/api/reviews/4")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(reviewJson);

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    void deleteShouldReturn204NoContent() throws Exception {
        Review review = new Review(1, 8.0, new Timestamp(System.currentTimeMillis()), null, 0, 0, 1, 1);

        when(repository.findById(1)).thenReturn(review);
        when(repository.deleteById(1)).thenReturn(true);


        var request = delete("/v1/api/reviews/1")
                .header("Authorization", "Bearer " + token);

        // Assert: Expecting 204 No Content as response
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404NotFoundWhenMissing() throws Exception {
        when(repository.deleteById(1)).thenReturn(false);

        var request = delete("/v1/api/reviews/1")
                .header("Authorization", "Bearer " + token);

        // Assert: Expecting 404 Not found as response
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
