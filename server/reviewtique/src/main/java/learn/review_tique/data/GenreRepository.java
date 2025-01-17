package learn.review_tique.data;

import learn.review_tique.models.Genre;


import java.util.List;

public interface GenreRepository {

    List<Genre> findAll();

    Genre findById(int genreId);

    Genre add(Genre genre);

    boolean update(Genre genre);

    boolean deleteById(int genreId);
}
