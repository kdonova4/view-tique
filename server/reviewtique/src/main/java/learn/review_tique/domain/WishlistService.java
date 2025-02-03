package learn.review_tique.domain;

import learn.review_tique.data.AppUserRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.data.WishlistRepository;
import learn.review_tique.models.Wishlist;
import org.springframework.stereotype.Service;

import java.util.List;

/*
        Wishlist Data
        ============
        - Game ID
            - Game ID MUST BE VALID
        - AppUser ID
            - AppUser ID MUST BE VALID


        public Wishlist findByUserId(int userId)
            // return repo findById() method

        public Result<Wishlist> add(Wishlist wishlist)
            // validate
            // if validate is not successful, then add message and type and return result
            // if ID is set, add message and type and return result
            // set wishlist to repo.add() method
            // set payload to the returned wishlist
            // return result

        public boolean deleteById(int wishlistId)
            //return repo call to deleteById()

        private Result<Wishlist> validate(Wishlist wishlist)
            // new result
            // check if Wishlist is null, if it is then add message and type and return result
            // check if gameId is NOT VALID, if not add message and type
            // check if userId is VALID, if not add message and type
            // return result

     */

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final GameRepository gameRepository;
    private final AppUserRepository appUserRepository;

    public WishlistService(WishlistRepository wishlistRepository, GameRepository gameRepository,
                           AppUserRepository appUserRepository) {
        this.wishlistRepository = wishlistRepository;
        this.gameRepository = gameRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<Wishlist> findByUserId(int userId) {
        return wishlistRepository.findByUserId(userId);
    }

    public Result<Wishlist> add(Wishlist wishlist) {
        Result<Wishlist> result = validate(wishlist);

        if(!result.isSuccess())
            return result;

        if(wishlist.getWishlistId() != 0) {
            result.addMessages("Wishlist ID CANNOT BE SET", ResultType.INVALID);
            return result;
        }

        wishlist = wishlistRepository.add(wishlist);
        result.setPayload(wishlist);
        return result;
    }

    public boolean deleteById(int wishlistId){
        return wishlistRepository.deleteById(wishlistId);
    }

    private Result<Wishlist> validate(Wishlist wishlist) {
        Result<Wishlist> result = new Result<>();

        if(wishlist == null) {
            result.addMessages("Wishlist CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(appUserRepository.findById(wishlist.getUserId()) == null) {
            result.addMessages("User ID MUST BE VALID", ResultType.INVALID);
        }

        if(gameRepository.findById(wishlist.getGameId()) == null) {
            result.addMessages("Game ID MUST BE VALID", ResultType.INVALID);
        }

        return result;
    }

}
