package learn.review_tique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.GameService;
import learn.review_tique.domain.Result;
import learn.review_tique.models.GameGenre;
import learn.review_tique.models.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/v1/game/genre")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "GameGenre Controller", description = "GameGenre Operations")
public class GameGenreController {

    private final GameService service;

    public GameGenreController(GameService service) {
        this.service = service;
    }

    @Operation(summary = "Find Genres for Game", description = "finds all genres for a game")
    @GetMapping("/{gameId}")
    public ResponseEntity<List<GameGenre>> findGenresByGameId(@PathVariable int gameId) {
        List<GameGenre> gameGenres = service.findGenresByGameId(gameId);

        if(gameGenres.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(gameGenres);
    }

    @Operation(summary = "Adds a Genre for a game", description = "Adds a genre to a game")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody GameGenre gameGenre) {
        Result<GameGenre> result = service.add(gameGenre);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Deletes a genre for a game", description = "Deletes a genre from a game")
    @DeleteMapping("/{gameId}/{genreId}")
    public ResponseEntity<Object> deleteById(@PathVariable int gameId ,@PathVariable int genreId) {
        if(service.deleteGenreById(gameId, genreId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
