package learn.review_tique.data;

import learn.review_tique.data.mappers.GenreMapper;
import learn.review_tique.models.Genre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GenreJdbcTemplateRepository implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    public GenreJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        final String sql = "select genre_id, genre_name from genre;";

        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre findById(int genreId) {
        final String sql = "select genre_id, genre_name from genre where genre_id = ?;";

        return jdbcTemplate.query(sql, new GenreMapper(), genreId).stream().findFirst().orElse(null);
    }

    @Override
    public List<Genre> searchByName(String genreName) {
        final String sql = "select genre_id, genre_name"
                + " from genre"
                + " where soundex(genre_name) = soundex(?);";

        return jdbcTemplate.query(sql, new GenreMapper(), genreName);
    }

    @Override
    public Genre add(Genre genre) {
        final String sql = "insert into genre (genre_name) "
                + "values (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, genre.getGenreName());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0)
            return null;

        genre.setGenreId(keyHolder.getKey().intValue());
        return genre;
    }

    @Override
    public boolean update(Genre genre) {
        final String sql = "update genre set"
                + " genre_name = ?"
                + " where genre_id = ?;";

        return jdbcTemplate.update(sql, genre.getGenreName(), genre.getGenreId()) > 0;
    }

    @Override
    public boolean deleteById(int genreId) {
        final String sql = "delete from genre where genre_id = ?;";

        return jdbcTemplate.update(sql, genreId) > 0;
    }
}
