package learn.review_tique.data;

import learn.review_tique.models.Game;

import java.util.List;

public interface GameRepository {

    List<Game> findAll();

    Game findById(int gameId);

    List<Game> searchGame(String gameName, int[] genreIds, int[] platformIds, Integer developerId);

    Game add(Game game);

    boolean update(Game game);

    boolean deleteById(int gameId);
}
