package learn.review_tique.data.mappers;

import learn.review_tique.data.DeveloperRepository;
import learn.review_tique.models.Developer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeveloperMapper implements RowMapper<Developer> {
    @Override
    public Developer mapRow(ResultSet resultSet, int i) throws SQLException {
        Developer developer = new Developer();
        developer.setDeveloperId(resultSet.getInt("developer_id"));
        developer.setDeveloperName(resultSet.getString("developer_name"));
        return developer;
    }
}
