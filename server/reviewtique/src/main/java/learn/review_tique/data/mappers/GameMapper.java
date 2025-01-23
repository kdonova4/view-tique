package learn.review_tique.data.mappers;

import learn.review_tique.models.Developer;
import learn.review_tique.models.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements RowMapper<Game> {

    @Override
    public Game mapRow(ResultSet resultSet, int i) throws SQLException {
        Game game = new Game();
        game.setGameId(resultSet.getInt("game_id"));
        game.setTitle(resultSet.getString("title"));
        game.setDescription(resultSet.getString("game_description"));
        game.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        game.setAvgUserScore(resultSet.getDouble("avg_user_score"));
        game.setAvgCriticScore(resultSet.getDouble("avg_critic_score"));
        game.setUserReviewCount(resultSet.getInt("user_review_count"));
        game.setCriticReviewCount(resultSet.getInt("critic_review_count"));

        DeveloperMapper developerMapper = new DeveloperMapper();
        game.setDeveloper(developerMapper.mapRow(resultSet, i));

        return game;
    }
}
