package learn.review_tique.data;

import learn.review_tique.data.mappers.GamePlatformMapper;
import learn.review_tique.models.GamePlatform;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class GamePlatformJdbcTemplateRepository implements GamePlatformRepository {

    private final JdbcTemplate jdbcTemplate;

    public GamePlatformJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(GamePlatform gamePlatform) {
        final String sql = "insert into game_platform (game_id, platform_id)"
                + " values (?, ?);";

        return jdbcTemplate.update(sql, gamePlatform.getGameId(), gamePlatform.getPlatform().getPlatformId()) > 0;
    }

    @Override
    public List<GamePlatform> findByGameId(int gameId) {
        final String sql = "select gp.game_id, gp.platform_id, p.platform_name"
                + " from game_platform gp"
                + " join platform p on gp.platform_id = p.platform_id"
                + " where gp.game_id = ?;";

        return jdbcTemplate.query(sql, new GamePlatformMapper(), gameId);
    }

    @Override
    public boolean deleteById(int gameId, int platformId) {
        final String sql = "delete from game_platform "
                + "where game_id = ? and platform_id = ?;";

        return jdbcTemplate.update(sql, gameId, platformId) > 0;
    }
}
