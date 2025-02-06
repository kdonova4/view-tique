package learn.review_tique.domain;

import learn.review_tique.data.GameGenreRepository;
import learn.review_tique.data.GamePlatformRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    GameService gameService;

    @MockBean
    GameRepository gameRepository;

    @MockBean
    GameGenreRepository gameGenreRepository;

    @MockBean
    GamePlatformRepository gamePlatformRepository;

    @Test
    void shouldFindAllGames() {
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);
        when(gameRepository.findAll()).thenReturn(
                List.of(game)
        );

        List<Game> actual = gameService.findAll();
        assertEquals(1, actual.size());
    }

    @Test
    void shouldFindGameById() {
        Developer developer = new Developer(5, "Eidos Montreal");
        Game expected = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(gameRepository.findById(1)).thenReturn(expected);
        Game actual = gameService.findById(1);
        assertEquals(expected, actual);
        verify(gameRepository).findById(1);
    }

    @Test
    void shouldFindGameBySearch() {
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(gameRepository.searchGame("deus ex", new int[]{2, 3}, null, 5)).thenReturn(
                List.of(game)
        );

        List<Game> actual = gameService.searchGame("deus ex", new int[]{2, 3}, null, 5);
        assertEquals(1, actual.size());
        verify(gameRepository).searchGame("deus ex", new int[]{2, 3}, null, 5);
    }

    @Test
    void shouldAdd() {
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(0, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);
        Game mockOut = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);



        when(gameRepository.add(game)).thenReturn(mockOut);
        Result<Game> actual = gameService.add(game);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(actual.getPayload(), mockOut);
    }

    @Test
    void shouldNotAddInvalid() {
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        Result<Game> actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setGameId(0);
        game.setTitle("");
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setTitle(null);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setTitle("Test");
        game.setDescription("");
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setDescription(null);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setDescription("TEST");
        game.setReleaseDate(LocalDate.now().plusDays(1));
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());


        game.setReleaseDate(LocalDate.now().minusDays(1));
        game.setAvgUserScore(-1.0);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setAvgUserScore(11.0);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setAvgUserScore(8.5);
        game.setAvgCriticScore(-8.0);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setAvgCriticScore(18.0);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setAvgCriticScore(8.0);
        game.setUserReviewCount(-5);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setUserReviewCount(10);
        game.setCriticReviewCount(-5);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setCriticReviewCount(30);
        game.setDeveloper(null);
        actual = gameService.add(game);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(gameRepository.update(game)).thenReturn(true);
        Result<Game> actual = gameService.update(game);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(2123, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(gameRepository.update(game)).thenReturn(false);
        Result<Game> actual = gameService.update(game);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateInvalid() {
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(0, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        Result<Game> actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setGameId(1);
        game.setTitle("");
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setTitle(null);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setTitle("Test");
        game.setDescription("");
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setDescription(null);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setDescription("TEST");
        game.setReleaseDate(LocalDate.now().plusDays(1));
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());


        game.setReleaseDate(LocalDate.now().minusDays(1));
        game.setAvgUserScore(-1.0);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setAvgUserScore(11.0);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setAvgUserScore(8.5);
        game.setAvgCriticScore(-8.0);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setAvgCriticScore(18.0);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setAvgCriticScore(8.0);
        game.setUserReviewCount(-5);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setUserReviewCount(10);
        game.setCriticReviewCount(-5);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());

        game.setCriticReviewCount(30);
        game.setDeveloper(null);
        actual = gameService.update(game);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldDelete() {


        when(gameRepository.deleteById(1)).thenReturn(true);
        Result<Game> actual = gameService.deleteGameById(1);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotDeleteMissing() {

        when(gameRepository.deleteById(2311)).thenReturn(false);
        Result<Game> actual = gameService.deleteGameById(2311);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldFindGenresByGameId() {
        Genre genre = new Genre(1, "Action");
        GameGenre gameGenre = new GameGenre(1, genre);

        when(gameGenreRepository.findByGameId(1)).thenReturn(
                List.of(gameGenre)
        );

        List<GameGenre> actual = gameService.findGenresByGameId(1);
        assertEquals(1, actual.size());
        verify(gameGenreRepository).findByGameId(1);
    }

    @Test
    void shouldAddGenre() {
        Genre genre = new Genre(1, "Action");
        GameGenre gameGenre = new GameGenre(1, genre);
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(0, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);
        when(gameRepository.findById(1)).thenReturn(game);
        when(gameGenreRepository.add(gameGenre)).thenReturn(true);
        Result<GameGenre> result = gameService.add(gameGenre);
        System.out.println(result.getMessages());
        assertTrue(result.isSuccess());
        verify(gameGenreRepository).add(gameGenre);
    }

    @Test
    void shouldNotAddInvalidGameGenre() {
        Genre genre = new Genre(1, "Action");
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(1, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);
        GameGenre gameGenre = new GameGenre(0, genre);
        when(gameRepository.findById(1)).thenReturn(game);
        Result<GameGenre> actual = gameService.add(gameGenre);
        assertEquals(ResultType.INVALID, actual.getType());

        gameGenre.setGameId(1);
        gameGenre.setGenre(null);
        actual = gameService.add(gameGenre);
        assertEquals(ResultType.INVALID, actual.getType());

        actual = gameService.add(new GameGenre());
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldDeleteGameGenre() {
        when(gameGenreRepository.deleteById(1, 1)).thenReturn(true);
        assertTrue(gameService.deleteGenreById(1, 1));
    }

    @Test
    void shouldNotDeleteMissingGameGenre() {
        when(gameGenreRepository.deleteById(2131, 14)).thenReturn(false);
        assertFalse(gameService.deleteGenreById(2131, 14));
    }

    @Test
    void findPlatformsByGameId() {
        Platform platform = new Platform(1, "Xbox");
        GamePlatform gamePlatform = new GamePlatform(1, platform);
        when(gamePlatformRepository.findByGameId(1)).thenReturn(
                List.of(gamePlatform)
        );

        List<GamePlatform> actual = gameService.findPlatformsByGameId(1);
        assertEquals(1, actual.size());
        verify(gamePlatformRepository).findByGameId(1);
    }

    @Test
    void shouldAddGamePlatform() {
        Platform platform = new Platform(1, "Xbox");
        GamePlatform gamePlatform = new GamePlatform(1, platform);
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(0, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);
        when(gameRepository.findById(1)).thenReturn(game);
        when(gamePlatformRepository.add(gamePlatform)).thenReturn(true);
        Result<GamePlatform> result = gameService.add(gamePlatform);
        System.out.println(result.getMessages());
        assertTrue(result.isSuccess());
        verify(gamePlatformRepository).add(gamePlatform);
    }

    @Test
    void shouldNotAddInvalidGamePlatform() {
        Platform platform = new Platform(1, "Xbox");
        GamePlatform gamePlatform = new GamePlatform(1, platform);
        Developer developer = new Developer(5, "Eidos Montreal");
        Game game = new Game(0, "Deus Ex: Mankind Divided", "Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy.",
                LocalDate.now(), 8.5, 7.9, 10, 30, developer);

        when(gameRepository.findById(1)).thenReturn(game);
        Result<GamePlatform> actual = gameService.add(gamePlatform);
        assertEquals(ResultType.INVALID, actual.getType());

        gamePlatform.setGameId(1);
        gamePlatform.setPlatform(null);
        actual = gameService.add(gamePlatform);
        assertEquals(ResultType.INVALID, actual.getType());

        actual = gameService.add(new GamePlatform());
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldDeleteGamePlatform() {
        when(gamePlatformRepository.deleteById(1, 1)).thenReturn(true);
        assertTrue(gameService.deletePlatformById(1, 1));
    }

    @Test
    void shouldNotDeleteMissingGamePlatform() {
        when(gamePlatformRepository.deleteById(2131, 14)).thenReturn(false);
        assertFalse(gameService.deletePlatformById(2131, 14));
    }
}
