package learn.review_tique.data.mappers;

import learn.review_tique.models.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AppUserMapper implements RowMapper<AppUser> {

    private List<String> roles;

    public AppUserMapper(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public AppUser mapRow(ResultSet resultSet, int i) throws SQLException {
        // Log the ResultSet values
        System.out.println("Mapping row: " + resultSet.getInt("app_user_id") + ", " +
                resultSet.getString("username") + ", " +
                resultSet.getString("password_hash") + ", " +
                resultSet.getBoolean("disabled"));


        return new AppUser(resultSet.getInt("app_user_id"),
                resultSet.getString("username"),
                resultSet.getString("password_hash"),
                resultSet.getBoolean("disabled"),
                roles);
    }
}
