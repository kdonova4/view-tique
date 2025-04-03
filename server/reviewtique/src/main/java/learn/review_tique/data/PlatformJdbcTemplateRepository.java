package learn.review_tique.data;

import learn.review_tique.data.mappers.PlatformMapper;
import learn.review_tique.models.Platform;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

@Repository
public class PlatformJdbcTemplateRepository implements PlatformRepository {

    private final JdbcTemplate jdbcTemplate;

    public PlatformJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Platform> findAll() {
        final String sql = "select platform_id, platform_name from platform;";

        return jdbcTemplate.query(sql, new PlatformMapper());
    }

    @Override
    public Platform findById(int platformId) {
        final String sql = "select platform_id, platform_name from platform"
                + " where platform_id = ?;";

        return jdbcTemplate.query(sql, new PlatformMapper(), platformId).stream().findFirst().orElse(null);
    }

    @Override
    public Platform findByName(String platformName) {
        final String sql = "select platform_id, platform_name" +
                " from platform" +
                " where platform_name = ?;";

        return jdbcTemplate.query(sql, new PlatformMapper(), platformName).stream().findFirst().orElse(null);
    }

    @Override
    public List<Platform> searchByName(String platformName) {
        final String sql = "select platform_id, platform_name"
                + " from platform"
                + " where match(platform_name) against(? in boolean mode);";

        if(platformName != null && !platformName.isBlank())
            return jdbcTemplate.query(sql, new PlatformMapper(), platformName + "*");
        else
            return Collections.emptyList();
    }

    @Override
    public Platform add(Platform platform) {
        final String sql = "insert into platform (platform_name) "
                + "values (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, platform.getPlatformName());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0)
            return null;

        platform.setPlatformId(keyHolder.getKey().intValue());
        return platform;
    }

    @Override
    public boolean update(Platform platform) {
        final String sql = "update platform set"
                + " platform_name = ?"
                + " where platform_id = ?;";

        return jdbcTemplate.update(sql, platform.getPlatformName(), platform.getPlatformId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int platformId) {
        final String gamePlatformSql = "delete from game_platform where platform_id = ?;";

        jdbcTemplate.update(gamePlatformSql, platformId);

        final String sql = "delete from platform where platform_id = ?;";

        return jdbcTemplate.update(sql, platformId) > 0;
    }
}
