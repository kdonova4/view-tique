package learn.review_tique.data.mappers;

import learn.review_tique.models.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre genre = new Genre();
        genre.setGenreId(resultSet.getInt("genre_id"));
        genre.setGenreName(resultSet.getString("genre_name"));
        return genre;
    }
}
