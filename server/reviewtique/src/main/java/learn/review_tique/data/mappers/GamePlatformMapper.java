package learn.review_tique.data.mappers;

import learn.review_tique.models.GamePlatform;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GamePlatformMapper implements RowMapper<GamePlatform> {
    @Override
    public GamePlatform mapRow(ResultSet resultSet, int i) throws SQLException {
        GamePlatform gamePlatform = new GamePlatform();
        gamePlatform.setGameId(resultSet.getInt("game_id"));

        PlatformMapper platformMapper = new PlatformMapper();
        gamePlatform.setPlatform(platformMapper.mapRow(resultSet, i));

        return gamePlatform;
    }
}
