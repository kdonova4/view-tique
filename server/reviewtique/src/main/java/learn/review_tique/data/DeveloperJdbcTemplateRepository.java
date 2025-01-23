package learn.review_tique.data;

import learn.review_tique.data.mappers.DeveloperMapper;
import learn.review_tique.models.Developer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

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
    public List<Developer> searchByName(String developerName) {
        return List.of();
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
    public boolean deleteById(int developerId) {
        final String sql = "delete from developer where developer_id = ?;";

        return jdbcTemplate.update(sql, developerId) > 0;
    }
}
