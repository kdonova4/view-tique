package learn.review_tique.data;

import learn.review_tique.models.GameGenre;

import java.util.List;

public interface GameGenreRepository {

   boolean add(GameGenre gameGenre);

   List<GameGenre> findByGameId(int gameId);

   boolean deleteById(int gameId, int genreId);
}
