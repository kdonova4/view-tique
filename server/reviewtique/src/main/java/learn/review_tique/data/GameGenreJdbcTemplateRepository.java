package learn.review_tique.data;

import learn.review_tique.data.mappers.GameGenreMapper;
import learn.review_tique.models.GameGenre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameGenreJdbcTemplateRepository implements GameGenreRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameGenreJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(GameGenre gameGenre) {
        final String sql = "insert into game_genre (game_id, genre_id)"
                + " values"
                + " (?, ?);";

        return jdbcTemplate.update(sql, gameGenre.getGameId(), gameGenre.getGenre().getGenreId()) > 0;
    }

    @Override
    public List<GameGenre> findByGameId(int gameId) {
        final String sql = "select gg.game_id, gg.genre_id, ge.genre_name"
                + " from game_genre gg"
                + " join genre ge on gg.genre_id = ge.genre_id"
                + " where gg.game_id = ?;";

        return jdbcTemplate.query(sql, new GameGenreMapper(), gameId);
    }

    @Override
    public boolean deleteById(int gameId, int genreId) {
        final String sql = "delete from game_genre "
                + "where game_id = ? and genre_id = ?;";

        return jdbcTemplate.update(sql, gameId, genreId) > 0;
    }
}
