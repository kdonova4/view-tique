package learn.review_tique.data.mappers;

import learn.review_tique.models.GameGenre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameGenreMapper implements RowMapper<GameGenre> {
    @Override
    public GameGenre mapRow(ResultSet resultSet, int i) throws SQLException {
        GameGenre gameGenre = new GameGenre();
        gameGenre.setGameId(resultSet.getInt("game_id"));

        GenreMapper genreMapper = new GenreMapper();
        gameGenre.setGenre(genreMapper.mapRow(resultSet, i));

        return gameGenre;
    }
}
