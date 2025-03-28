package learn.review_tique.data;

import learn.review_tique.data.mappers.DeveloperMapper;
import learn.review_tique.data.mappers.GenreMapper;
import learn.review_tique.models.Developer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DeveloperJdbcTemplateRepository implements DeveloperRepository{

    private final JdbcTemplate jdbcTemplate;

    public DeveloperJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Developer> findAll() {
        final String sql = "select developer_id, developer_name from developer;";

        return jdbcTemplate.query(sql, new DeveloperMapper());
    }

    @Override
    public Developer findById(int developerId) {
        final String sql = "select developer_id, developer_name from developer where developer_id = ?;";

        return jdbcTemplate.query(sql, new DeveloperMapper(), developerId).stream().findFirst().orElse(null);
    }

    @Override
    public Developer findByName(String developerName) {
        final String sql = "select developer_id, developer_name from developer where developer_name = ?;";

        return jdbcTemplate.query(sql, new DeveloperMapper(), developerName).stream().findFirst().orElse(null);
    }

    @Override
    public List<Developer> searchByName(String developerName) {
        final String sql = "select developer_id, developer_name"
                + " from developer"
                + " where soundex(developer_name) = soundex(?);";

        return jdbcTemplate.query(sql, new DeveloperMapper(), developerName);
    }

    @Override
    public Developer add(Developer developer) {
        final String sql = "insert into developer (developer_name)"
                + " values (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, developer.getDeveloperName());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0)
            return null;

        developer.setDeveloperId(keyHolder.getKey().intValue());
        return developer;
    }

    @Override
    public boolean update(Developer developer) {
        final String sql = "update developer set"
                + " developer_name = ?"
                + " where developer_id = ?;";
        return jdbcTemplate.update(sql, developer.getDeveloperName(), developer.getDeveloperId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int developerId) {
        final String gameGenreSql = "delete from game_genre where game_id in (select game_id from game where developer_id = ?);";
        final String gamePlatformSql = "delete from game_platform where game_id in (select game_id from game where developer_id = ?);";
        final String gameSql = "delete from game where developer_id = ?;";
        final String reviewSql = "delete from review where game_id in (select game_id from game where developer_id = ?);";
        final String reviewReactionSql = "delete from review_reaction where review_id in (select review_id from review where game_id in (select game_id from game where developer_id = ?));";
        final String wishlistSql = "delete from wishlist where game_id in (select game_id from game where developer_id = ?);";

        jdbcTemplate.update(gameGenreSql, developerId);
        jdbcTemplate.update(gamePlatformSql, developerId);
        jdbcTemplate.update(reviewReactionSql, developerId);
        jdbcTemplate.update(reviewSql, developerId);
        jdbcTemplate.update(wishlistSql, developerId);
        jdbcTemplate.update(gameSql, developerId);


        final String sql = "delete from developer where developer_id = ?;";

        return jdbcTemplate.update(sql, developerId) > 0;
    }
}
