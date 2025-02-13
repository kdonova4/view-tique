package learn.review_tique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.GameService;
import learn.review_tique.domain.Result;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.Game;
import learn.review_tique.security.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;


@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/v1/api/games")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Game Controller", description = "Game Operations")
public class GameController {

    private final GameService gameService;
    private final AppUserService appUserService;

    public GameController(GameService gameService, AppUserService appUserService) {
        this.gameService = gameService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<Game> findAll()  {
        return gameService.findAll();
    }

    @Operation(summary = "Get Game by ID", description = "Finds Game from ID")
    @GetMapping("/{gameId}")
    public ResponseEntity<Game> findById(@PathVariable int gameId) {
        Game game = gameService.findById(gameId);
        if(game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(game);
    }

    @Operation(summary = "Search For Games", description = "Search for games using filters")
    @GetMapping("/search")
    public ResponseEntity<List<Game>> searchGame(@RequestParam(required = false) String gameName,
                                                 @RequestParam(required = false) Integer[] genres,
                                                 @RequestParam(required = false) Integer[] platforms,
                                                 @RequestParam(required = false) Integer developerId) {

        int[] genresArray;
        int[] platformArray;
        if(genres != null) {
            genresArray = Arrays.stream(genres).mapToInt(Integer::intValue).toArray();
        } else {
            genresArray = null;
        }
        if(platforms != null) {
            platformArray = Arrays.stream(platforms).mapToInt(Integer::intValue).toArray();
        } else {
            platformArray = null;
        }


        List<Game> games = gameService.searchGame(gameName, genresArray, platformArray, developerId);

        if(games.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(games);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a Game", description = "Adding a Game to the database, REQUIRES ADMIN ROLE")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Game game) {
        Result<Game> result = gameService.add(game);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a Game", description = "Updates a Game to the database, REQUIRES ADMIN ROLE")
    @PutMapping("/{gameId}")
    public ResponseEntity<Object> update(@PathVariable int gameId, @RequestBody Game game) {
        if(gameId != game.getGameId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Game> result = gameService.update(game);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a Game", description = "Deletes a Game to the database, REQUIRES ADMIN ROLE")
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Object> deleteById(@PathVariable int gameId) {


        Result<Game> result = gameService.deleteGameById(gameId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
