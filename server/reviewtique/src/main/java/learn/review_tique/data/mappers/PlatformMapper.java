package learn.review_tique.data.mappers;

import learn.review_tique.models.Platform;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlatformMapper implements RowMapper<Platform> {
    @Override
    public Platform mapRow(ResultSet resultSet, int i) throws SQLException {
        Platform platform = new Platform();
        platform.setPlatformId(resultSet.getInt("platform_id"));
        platform.setPlatformName(resultSet.getString("platform_name"));
        return platform;
    }
}
