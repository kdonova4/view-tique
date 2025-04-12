package learn.review_tique.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.GenreRepository;
import learn.review_tique.data.PlatformRepository;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.Genre;
import learn.review_tique.models.Platform;
import learn.review_tique.security.JwtConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GenreControllerTest {

    @MockBean
    GenreRepository repository;

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

        var request = post("/api/v1/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        Genre genre = new Genre();
        String amenityJson = jsonMapper.writeValueAsString(genre);

        var request = post("/api/v1/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(amenityJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void appShouldReturn415WhenMultipart() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        Genre genre = new Genre(0, "Test");
        Genre expected = new Genre(1, "Test");
        AppUser appUser = new AppUser(1, "test_user1", "85c*98Kd", false,
                List.of("USER"));

        when(appUserRepository.findById(1)).thenReturn(appUser);
        when(repository.add(any())).thenReturn(expected);

        String amenityJson = jsonMapper.writeValueAsString(genre);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        var request = post("/api/v1/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(amenityJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void deleteShouldReturn204NoContent() throws Exception {


        when(repository.deleteById(1)).thenReturn(true);


        var request = delete("/api/v1/genres/1")
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404NotFoundWhenMissing() throws Exception {
        when(repository.deleteById(1)).thenReturn(false);

        var request = delete("/api/v1/genres/1")
                .header("Authorization", "Bearer " + token);

        // Assert: Expecting 404 Not found as response
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
