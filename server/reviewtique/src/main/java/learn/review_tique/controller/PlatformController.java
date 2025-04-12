package learn.review_tique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.PlatformService;
import learn.review_tique.domain.Result;
import learn.review_tique.models.Platform;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/v1/platforms")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Platform Controller", description = "Platform Operations")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @Operation(summary = "find a platform", description = "find a platform by its ID")
    @GetMapping("/{platformId}")
    public ResponseEntity<Platform> findById(@PathVariable int platformId) {
        Platform platform = platformService.findById(platformId);

        if(platform == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(platform);
    }

    @Operation(summary = "search for a platform", description = "search for a platform by its name")
    @GetMapping("/searchPlatform")
    public ResponseEntity<List<Platform>> searchByName(@RequestParam String platformName) {
        List<Platform> platforms = platformService.searchByName(platformName);

        if(platforms.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(platforms);
    }

    @Operation(summary = "Add a platform", description = "Adds a platform")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Platform platform) {
        Result<Platform> result = platformService.add(platform);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Delete a platform", description = "Deletes a platform")
    @DeleteMapping("{platformId}")
    public ResponseEntity<Object> deleteById(@PathVariable int platformId) {
        if(platformService.deleteById(platformId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
