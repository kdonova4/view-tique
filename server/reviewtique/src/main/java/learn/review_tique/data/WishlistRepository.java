package learn.review_tique.data;

import learn.review_tique.models.Wishlist;

import java.util.List;

public interface WishlistRepository {

    List<Wishlist> findByUserId(int userId);

    Wishlist add(Wishlist wishlist);

    boolean deleteById(int wishlistId);
}
