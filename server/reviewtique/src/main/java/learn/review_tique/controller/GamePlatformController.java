package learn.review_tique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.GameService;
import learn.review_tique.domain.Result;
import learn.review_tique.models.GameGenre;
import learn.review_tique.models.GamePlatform;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/v1/api/game/platform")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "GamePlatform Controller", description = "GamePlatform Operations")
public class GamePlatformController {

    private final GameService service;

    public GamePlatformController(GameService service) {
        this.service = service;
    }

    @Operation(summary = "Find Platforms for Game", description = "finds all platforms for a game")
    @GetMapping("/{gameId}")
    public ResponseEntity<List<GamePlatform>> findPlatformsByGameId(@PathVariable int gameId) {
        List<GamePlatform> gamePlatforms = service.findPlatformsByGameId(gameId);

        if (service.findById(gameId) == null) {
            return ResponseEntity.notFound().build();
        } else if(gamePlatforms.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(gamePlatforms);
    }

    @Operation(summary = "Adds a Platform for a game", description = "Adds a platform to a game")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody GamePlatform gamePlatform) {
        Result<GamePlatform> result = service.add(gamePlatform);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Deletes a Platform for a game", description = "Deletes a platform from a game")
    @DeleteMapping("/{gameId}/{platformId}")
    public ResponseEntity<Object> deleteById(@PathVariable int gameId ,@PathVariable int platformId) {
        if(service.deletePlatformById(gameId, platformId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
