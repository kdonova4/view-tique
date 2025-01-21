package learn.review_tique.data;

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
        return false;
    }

    @Override
    public List<GamePlatform> findByGameId(int gameId) {
        return List.of();
    }

    @Override
    public boolean deleteById(int gameId, int platformId) {
        return false;
    }
}
