package learn.review_tique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.GenreService;
import learn.review_tique.domain.Result;
import learn.review_tique.models.Genre;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/v1/api/genres")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Genre Controller", description = "Genre Operations")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(summary = "Finds a genre", description = "Finds a genre from ID")
    @GetMapping("{genreId}")
    public ResponseEntity<Genre> findById(@PathVariable int genreId) {
        Genre genre = genreService.findById(genreId);

        if(genre == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(genre);
    }

    @Operation(summary = "search for a genre", description = "search for a genre by its name")
    @GetMapping("/searchGenre")
    public ResponseEntity<List<Genre>> searchByName(@RequestParam String genreName) {
        List<Genre> genres = genreService.searchByName(genreName);

        if(genres.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(genres);
    }

    @Operation(summary = "Add a genre", description = "Adds a genre")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Genre genre) {
        Result<Genre> result = genreService.add(genre);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Delete a genre", description = "Deletes a genre")
    @DeleteMapping("{genreId}")
    public ResponseEntity<Object> deleteById(@PathVariable int genreId) {
        if(genreService.deleteById(genreId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
