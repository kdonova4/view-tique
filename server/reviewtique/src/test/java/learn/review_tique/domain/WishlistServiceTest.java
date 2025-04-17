package learn.review_tique.domain;

import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.data.WishlistRepository;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.Developer;
import learn.review_tique.models.Game;
import learn.review_tique.models.Wishlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest()
public class WishlistServiceTest {

    @Autowired
    WishlistService service;

    @MockBean
    WishlistRepository wishlistRepository;

    @MockBean
    GameRepository gameRepository;

    @MockBean
    AppUserRepository appUserRepository;

    @Test
    void findByUserId() {
        Wishlist wishlist = new Wishlist(1, 1, 1);

        when(wishlistRepository.findByUserId(1)).thenReturn(
                List.of(wishlist)
        );

        List<Wishlist> actual = service.findByUserId(1);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldAdd() {
        Wishlist wishlist = new Wishlist(0, 1, 1);
        Wishlist mockOut = new Wishlist(1, 1, 1);
        AppUser user = new AppUser(1, "test_user1", "$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y", false, List.of("user"));
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 150, 30, developer);

        when(appUserRepository.findById(1)).thenReturn(user);
        when(gameRepository.findById(1)).thenReturn(game);
        when(wishlistRepository.add(wishlist)).thenReturn(mockOut);

        Result<Wishlist> actual = service.add(wishlist);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddInvalid() {
        Wishlist wishlist = new Wishlist(1, 1, 1);

        Result<Wishlist> actual = service.add(wishlist);
        assertEquals(ResultType.INVALID, actual.getType());

        wishlist.setWishlistId(0);
        wishlist.setGameId(0);
        actual = service.add(wishlist);
        assertEquals(ResultType.INVALID, actual.getType());

        wishlist.setGameId(1);
        wishlist.setUserId(0);
        actual = service.add(wishlist);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldDelete() {
        when(wishlistRepository.deleteById(1)).thenReturn(true);
        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissing() {
        when(wishlistRepository.deleteById(1)).thenReturn(false);
        assertFalse(service.deleteById(1));
    }


}
