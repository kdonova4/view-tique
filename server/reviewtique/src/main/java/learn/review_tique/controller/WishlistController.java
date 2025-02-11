package learn.review_tique.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.review_tique.domain.Result;
import learn.review_tique.domain.WishlistService;
import learn.review_tique.models.AppUser;
import learn.review_tique.models.Review;
import learn.review_tique.models.Wishlist;
import learn.review_tique.security.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/v1/api/wishlists")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Wishlist Controller", description = "Wishlist Operations")
public class WishlistController {

    private final WishlistService service;
    private final AppUserService appUserService;

    public WishlistController(WishlistService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @Operation(summary = "Get games on wishlist by User ID", description = "finds all games on a user's wishlist")
    @GetMapping("/wishlist")
    public ResponseEntity<List<Wishlist>> findByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        AppUser user = (AppUser) appUserService.loadUserByUsername(username);

        int authenticatedUserId = user.getAppUserId();

        List<Wishlist> wishlists = service.findByUserId(authenticatedUserId);

        if(wishlists.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(wishlists);
    }

    @Operation(summary = "Add to wishlist", description = "adds a game to a users wishlist")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Wishlist wishlist) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        AppUser user = (AppUser) appUserService.loadUserByUsername(username);

        int authenticatedUserId = user.getAppUserId();

        if(wishlist.getUserId() != authenticatedUserId) {
            return new ResponseEntity<>("Not authorized to add to another users wishlist", HttpStatus.FORBIDDEN);
        }

        Result<Wishlist> result = service.add(wishlist);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @Operation(summary = "Delete from a wishlist", description = "deletes a game from a users wishlist")
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Object> deleteById(@PathVariable int wishlistId) {
        if(service.deleteById(wishlistId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
