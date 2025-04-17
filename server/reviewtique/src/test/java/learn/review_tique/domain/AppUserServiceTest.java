package learn.review_tique.domain;

import learn.review_tique.data.AppUserRepository;
import learn.review_tique.models.AppUser;
import learn.review_tique.security.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AppUserServiceTest {

    @Autowired
    AppUserService service;

    @MockBean
    AppUserRepository appUserRepository;

    @MockBean
    PasswordEncoder encoder;

    @Test
    void shouldLoadByUsername() {
        AppUser appUser = new AppUser(1,"test_user1", "85c*98Kd", false, List.of("User"));

        when(appUserRepository.findByUsername("test_user1")).thenReturn(appUser);
        UserDetails actual = service.loadUserByUsername("test_user1");

        assertEquals("85c*98Kd", actual.getPassword());
    }

    @Test
    void shouldCreate() {
        // Arrange
        String username = "test_user1";
        String password = "85c*98Kd";
        String encodedPassword = "$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y";  // Mocked encoded password

        // Mocking the PasswordEncoder to return the encoded password
        when(encoder.encode(password)).thenReturn(encodedPassword);

        // Creating the expected AppUser object with the encoded password
        AppUser appUser = new AppUser(0, username, encodedPassword, false, List.of("User"));

        // Mocking the repository create method
        when(appUserRepository.create(appUser)).thenReturn(appUser);

        // Act
        AppUser actual = service.create(username, password);

        // Assert
        assertNotNull(actual);
        assertEquals(username, actual.getUsername());
        assertEquals(encodedPassword, actual.getPassword());  // Check the encoded password
        assertTrue(actual.isEnabled());  // Check the default value for enabled
    }
}
