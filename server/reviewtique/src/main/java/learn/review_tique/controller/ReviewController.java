package learn.review_tique.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.Result;
import learn.review_tique.domain.ReviewService;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.Review;
import learn.review_tique.security.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;



@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/v1/reviews")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Review Controller", description = "Review Operations")
public class ReviewController {
    private final AppUserService appUserService;
    private final ReviewService reviewService;

    public ReviewController(AppUserService appUserService, ReviewService reviewService) {
        this.appUserService = appUserService;
        this.reviewService = reviewService;
    }

    @Operation(summary = "Get Review", description = "Retrieves a review by ID")
    @GetMapping("/{reviewId}")
    public Review findById(@PathVariable int reviewId) {
        return reviewService.findById(reviewId);
    }

    @Operation(summary = "Get Reviews for User", description = "Retrieves all reviews for current user")
    @GetMapping("/me")
    public ResponseEntity<List<Review>> findByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        AppUser user = (AppUser) appUserService.loadUserByUsername(username);

        int authenticatedUserId = user.getAppUserId();

        List<Review> reviews = reviewService.findByUserId(authenticatedUserId);

        if(reviews.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Get Reviews for Game", description = "Retrieves all reviews for a game")
    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<Review>> findByGameId(@PathVariable int gameId) {
        List<Review> reviews = reviewService.findByGameId(gameId);
        if(reviews.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("hasAnyRole('CRITIC', 'USER')")
    @Operation(summary = "Add Review for Game", description = "Adds a review for a game")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        AppUser user = (AppUser) appUserService.loadUserByUsername(username);

        int authenticatedUserId = user.getAppUserId();

        if(review.getUserId() != authenticatedUserId) {
            return new ResponseEntity<>("Not authorized to create a review for another user", HttpStatus.FORBIDDEN);
        }

        Result<Review> result = reviewService.add(review);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Update Review For a Game", description = "Updates a review for a game")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Object> update(@PathVariable int reviewId, @RequestBody Review review) {
        if(reviewId != review.getReviewId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        AppUser user = (AppUser) appUserService.loadUserByUsername(username);

        int authenticatedUserId = user.getAppUserId();

        if(review.getUserId() != authenticatedUserId) {
            return new ResponseEntity<>("Not authorized to update a review for another user", HttpStatus.FORBIDDEN);
        }

        Result<Review> result = reviewService.update(review);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Delete Review For a Game", description = "Deletes a review for a game")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Object> deleteById(@PathVariable int reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        AppUser user = (AppUser) appUserService.loadUserByUsername(username);

        int authenticatedUserId = user.getAppUserId();

        Review review = reviewService.findById(reviewId);

        if(review != null && review.getUserId() != authenticatedUserId) {
            return new ResponseEntity<>("Not authorized to delete a review for another user", HttpStatus.FORBIDDEN);
        }

        Result<Review> result = reviewService.deleteById(reviewId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}


