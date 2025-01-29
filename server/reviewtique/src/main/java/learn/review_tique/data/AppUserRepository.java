package learn.review_tique.data;

import learn.review_tique.models.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;

public interface AppUserRepository {

    AppUser findByUsername(String username);

    AppUser findById(int userId);

    AppUser create(AppUser user);

    void update(AppUser user);
}
