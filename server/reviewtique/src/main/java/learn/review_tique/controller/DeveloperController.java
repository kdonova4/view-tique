package learn.review_tique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.DeveloperService;
import learn.review_tique.domain.Result;
import learn.review_tique.models.Developer;
import learn.review_tique.models.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000",
        "http://review-tique-frontend.s3-website.us-east-2.amazonaws.com"})
@RequestMapping("/api/v1/developers")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Developer Controller", description = "Developer Operations")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @Operation(summary = "Finds a developer", description = "Finds a developer from ID")
    @GetMapping("/{developerId}")
    public ResponseEntity<Developer> findById(@PathVariable int developerId) {
        Developer developer = developerService.findById(developerId);

        if(developer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(developer);
    }

    @Operation(summary = "search for a developer", description = "search for a developer by its name")
    @GetMapping("/searchDeveloper")
    public ResponseEntity<List<Developer>> searchByName(@RequestParam String developerName) {
        List<Developer> developers = developerService.searchByName(developerName);

        if(developers.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(developers);
    }

    @Operation(summary = "Add a developer", description = "Adds a developer")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Developer developer) {
        Result<Developer> result = developerService.add(developer);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Delete a developer", description = "Deletes a developer")
    @DeleteMapping("/{developerId}")
    public ResponseEntity<Object> deleteById(@PathVariable int developerId) {
        if(developerService.deleteById(developerId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
