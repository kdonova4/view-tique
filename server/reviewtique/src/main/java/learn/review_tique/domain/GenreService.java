package learn.review_tique.domain;


import learn.review_tique.data.GenreRepository;
import learn.review_tique.models.Genre;
import org.springframework.stereotype.Service;

import java.util.List;

/*
        Genre Data
        ============
        - Genre Name
            - Genre Name CANNOT BE NULL OR BLANK


        public List<Genre> findAll()
            // return repo findAll() method

        public Genre findById(int genreId)
            // return repo findById() method

        public List<Genre> searchByName(String genreId)
            // return repo searchByName() method

        public Result<Genre> add(Genre genre)
            // validate
            // if validate is not successful, then add message and type and return result
            // if ID is set, add message and type and return result
            // set Genre to repo.add() method
            // set payload to the returned Genre
            // return result

        public Result<Genre> update(Genre genre)
            // validate
            // if validate not successful, add message and type then return result
            // check if ID is not set, if yes then add message and type and return result
            // if repo call to update() method is unsuccessful add message and type
            // return result

        public boolean deleteById(int genreId)
            //return repo call to deleteById()

        private Result<Genre> validate(Genre genre)
            // new result
            // check if Genre is null, if it is then add message and type and return result
            // check if Genre Name is NULL or BLANK, if it is then add message and type

     */
@Service
public class GenreService {

    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public List<Genre> findAll() {
        return repository.findAll();
    }

    public Genre findById(int genreId) {
        return repository.findById(genreId);
    }

    public Genre findByName(String genreName) {
        return repository.findByName(genreName);
    }

    public List<Genre> searchByName(String genreName) {
        return repository.searchByName(genreName);
    }

    public Result<Genre> add(Genre genre) {
        Result<Genre> result = validate(genre);

        if(!result.isSuccess())
            return result;

        if(genre.getGenreId() != 0) {
            result.addMessages("Genre ID CANNOT BE SET", ResultType.INVALID);
            return result;
        }

        genre = repository.add(genre);
        result.setPayload(genre);
        return result;
    }

    public Result<Genre> update(Genre genre) {
        Result<Genre> result = validate(genre);

        if(!result.isSuccess())
            return result;

        if(genre.getGenreId() <= 0) {
            result.addMessages("Genre ID MUST BE SET", ResultType.INVALID);
            return result;
        }

        if(!repository.update(genre)) {
            result.addMessages(String.format("Genre ID %s NOT FOUND", genre.getGenreId()), ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int genreId) {
        return repository.deleteById(genreId);
    }

    private Result<Genre> validate(Genre genre) {
        Result<Genre> result = new Result<>();

        if(genre == null) {
            result.addMessages("Genre CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(genre.getGenreName() == null || genre.getGenreName().isBlank()) {
            result.addMessages("Genre Name is REQUIRED", ResultType.INVALID);
        }

        return result;
    }

}
