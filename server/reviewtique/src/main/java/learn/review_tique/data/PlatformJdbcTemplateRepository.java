package learn.review_tique.data;

import learn.review_tique.models.Platform;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class PlatformJdbcTemplateRepository implements PlatformRepository {

    private final JdbcTemplate jdbcTemplate;

    public PlatformJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Platform> findAll() {
        return List.of();
    }

    @Override
    public Platform findById(int platformId) {
        return null;
    }

    @Override
    public Platform add(Platform platform) {
        return null;
    }

    @Override
    public boolean update(Platform platform) {
        return false;
    }

    @Override
    public boolean deleteById(int platformId) {
        return false;
    }
}
