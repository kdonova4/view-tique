package learn.review_tique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.Result;
import learn.review_tique.domain.ReviewReactionService;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.Review;
import learn.review_tique.models.ReviewReaction;
import learn.review_tique.security.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/v1/api/reactions")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Review Reaction Controller", description = "Review Reaction Operations")
public class ReviewReactionController {

    private final ReviewReactionService reactionService;
    private  final AppUserService appUserService;

    public ReviewReactionController(ReviewReactionService reactionService, AppUserService appUserService) {
        this.reactionService = reactionService;
        this.appUserService = appUserService;
    }

    @Operation(summary = "Find Reaction", description = "Finds Review Reaction by ID")
    @GetMapping("/{reactionId}")
    public ResponseEntity<ReviewReaction> findById(@PathVariable int reactionId) {
        ReviewReaction reviewReaction = reactionService.findById(reactionId);
        if(reviewReaction != null) {
            return ResponseEntity.ok(reviewReaction);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Find Reaction From Review ID and User ID", description = "Finds Review Reaction by Review ID and UserID")
    @GetMapping("/{reviewId}/{userId}")
    public ResponseEntity<ReviewReaction> findByReviewIdAndUserId(@PathVariable int reviewId, @PathVariable int userId) {
        ReviewReaction reviewReaction = reactionService.findByReviewAndUserId(reviewId, userId);
        if(reviewReaction != null) {
            return ResponseEntity.ok(reviewReaction);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create Reaction ", description = "Creates a Reaction for a review")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody ReviewReaction reviewReaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        AppUser user = (AppUser) appUserService.loadUserByUsername(username);

        int authenticatedUserId = user.getAppUserId();

        if(reviewReaction.getUserId() != authenticatedUserId) {
            return new ResponseEntity<>("Not authorized to create a reaction for another user", HttpStatus.FORBIDDEN);
        }

        Result<ReviewReaction> result = reactionService.add(reviewReaction);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Delete Reaction ", description = "Deletes a Reaction from a review")
    @DeleteMapping("/{reactionId}")
    public ResponseEntity<Object> deleteById(@PathVariable int reactionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        AppUser user = (AppUser) appUserService.loadUserByUsername(username);

        int authenticatedUserId = user.getAppUserId();

        ReviewReaction reviewReaction = reactionService.findById(reactionId);
        if(reviewReaction != null && reviewReaction.getUserId() != authenticatedUserId) {
            return new ResponseEntity<>("Not authorized to delete a reaction for another user", HttpStatus.FORBIDDEN);
        }

        if(reactionService.deleteById(reactionId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
