package learn.review_tique.domain;

import learn.review_tique.data.GameGenreRepository;
import learn.review_tique.data.GamePlatformRepository;
import learn.review_tique.data.GameRepository;
import learn.review_tique.models.Game;
import learn.review_tique.models.GameGenre;
import learn.review_tique.models.GamePlatform;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/*
        Game Data
        ============
        - Game Title
            - Game Title CANNOT BE NULL OR BLANK
        - Game Description
            - Game Description CANNOT BE NULL OR BLANK
        - Release Date
            - Release Date CANNOT BE NULL
            - Release DATE MUST BE IN THE PAST
        - Avg User Score
            - MUST BE BETWEEN 0.0 AND 10.0
        - Avg Critic Score
            - MUST BE BETWEEN 0.0 AND 10.0
        - User Review Count
            - MUST BE GREATER OR EQUAL TO 0
        - Critic Review Count
            - MUST BE GREATER OR EQUAL TO 0
        - Developer ID
            - MUST BE VALID
        - Other
            - NO DUPLICATE GAMES | Game Title <-> Developer ID

        public List<Game> findAll()
            // return call to repo method

        public Game findById(int gameId)
            // return call to repo method

        public List<Game> searchGame(String gameName, int[] genreIds, int[] platformIds, Integer developerId)
            // return call to repo method

        public Result<Game> add(Game game)
            // validate
            // return result if validate failed
            // check if ID is not zero, if true then add message and invalid type and return result
            // parameter is now equal to repo.add()
            // set the result payload to the repo.add() product
            // return result

        public Result<Game> update(Game game)
            // validate
            // return result if validate failed
            // check if id is less than or equal to 0, if yes then add message and invalid type and return result
            // check if update was successful, if not, then add message and not found type
            // return result

        public Result<Game> deleteById(int gameId)
            // initialize result
            // check if delete successful and if not thhen add message and not found type
            // return result;

     */

@Service
public class GameService {

    private final GameRepository repository;
    private final GameGenreRepository gameGenreRepository;
    private final GamePlatformRepository gamePlatformRepository;

    public GameService(GameRepository repository, GameGenreRepository gameGenreRepository, GamePlatformRepository gamePlatformRepository) {
        this.repository = repository;
        this.gameGenreRepository = gameGenreRepository;
        this.gamePlatformRepository = gamePlatformRepository;
    }

    public List<Game> findAll() {
        return repository.findAll();
    }

    public Game findById(int gameId) {
        return repository.findById(gameId);
    }

    public List<Game> searchGame(String gameName, int[] genreIds, int[] platformIds, Integer developerId) {
        return repository.searchGame(gameName, genreIds, platformIds, developerId);
    }

    public Result<Game> add(Game game) {
        Result<Game> result = validate(game);

        if(!result.isSuccess()) {
            return result;
        }

        if(game.getGameId() != 0) {
            result.addMessages("Game ID CANNOT BE SET for `ADD` Operation", ResultType.INVALID);
            return result;
        }

        game = repository.add(game);
        result.setPayload(game);
        return result;
    }

    public Result<Game> update(Game game) {
        Result<Game> result = validate(game);

        if(!result.isSuccess()) {
            return result;
        }

        if(game.getGameId() <= 0) {
            result.addMessages("Game ID MUST BE SET for `UPDATE` Operation", ResultType.INVALID);
            return result;
        }

        if(!repository.update(game)) {
            result.addMessages(String.format("Game ID %s NOT FOUND", game.getGameId()), ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<Game> deleteGameById(int gameId) {
        Result<Game> result = new Result<>();

        if(!repository.deleteById(gameId)) {
            result.addMessages(String.format("Game ID %s NOT FOUND", gameId), ResultType.NOT_FOUND);
        }

        return result;
    }



    private Result<Game> validate(Game game) {
        Result<Game> result = new Result<>();

        if(game == null) {
            result.addMessages("Game CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(game.getTitle() == null || game.getTitle().isBlank()) {
            result.addMessages("Game Title IS REQUIRED", ResultType.INVALID);
        }

        if(game.getDescription() == null || game.getDescription().isBlank()) {
            result.addMessages("Game Description IS REQUIRED", ResultType.INVALID);
        }

        if(game.getReleaseDate() == null) {
            result.addMessages("Game Release Date IS REQUIRED", ResultType.INVALID);
        } else if (game.getReleaseDate().isAfter(LocalDate.now())) {
            result.addMessages("Game Release Date CANNOT BE IN THE FUTURE", ResultType.INVALID);
        }

        if(game.getAvgUserScore() < 0.0 || game.getAvgUserScore() > 10.0) {
            result.addMessages("Average User Score MUST BE BETWEEN 0.0 AND 10.0", ResultType.INVALID);
        }

        if(game.getAvgCriticScore() < 0.0 || game.getAvgCriticScore() > 10.0) {
            result.addMessages("Average Critic Score MUST BE BETWEEN 0.0 AND 10.0", ResultType.INVALID);
        }

        if(game.getUserReviewCount() < 0) {
            result.addMessages("User Review Count MUST BE GREATER OR EQUAL TO 0", ResultType.INVALID);
        }

        if(game.getCriticReviewCount() < 0) {
            result.addMessages("Critic Review Count MUST BE GREATER OR EQUAL TO 0", ResultType.INVALID);
        }

        if(game.getDeveloper() == null) {
            result.addMessages("Developer CANNOT BE NULL", ResultType.INVALID);
        }

        return result;
    }

    /*
        Game-Genre Data
        ============
        - Game ID
            - Game ID MUST BE VALID
        - Genre
            - Genre CANNOT BE NULL
        - NO DUPLICATE GENRE FOR GAMES
     */

    public List<GameGenre> findGenresByGameId(int gameId) {
        return gameGenreRepository.findByGameId(gameId);
    }

    public Result<GameGenre> add(GameGenre gameGenre) {
        Result<GameGenre> result = validate(gameGenre);

        if(!result.isSuccess()) {
            return result;
        }

        if(!gameGenreRepository.add(gameGenre)) {
            result.addMessages("Genre Not Added", ResultType.INVALID);
        }

        return result;
    }

    public boolean deleteGenreById(int gameId, int genreId) {
        return gameGenreRepository.deleteById(gameId, genreId);
    }

    private Result<GameGenre> validate(GameGenre gameGenre) {
        Result<GameGenre> result = new Result<>();

        if(gameGenre == null) {
            result.addMessages("GameGenre CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if (gameGenre.getGameId() <= 0) {
            result.addMessages("Valid Game ID is REQUIRED", ResultType.INVALID);
        } else if (repository.findById(gameGenre.getGameId()) == null) {
            result.addMessages("Game does not exist", ResultType.INVALID);
        }

        if(gameGenre.getGenre() == null) {
            result.addMessages("Genre CANNOT BE NULL", ResultType.INVALID);
        }

        List<GameGenre> genres = gameGenreRepository.findByGameId(gameGenre.getGameId());
        for(GameGenre gg : genres) {
            if(gg.getGenre().getGenreId() == gameGenre.getGenre().getGenreId()) {
                result.addMessages("This Genre already is associated with this game", ResultType.INVALID);
                return result;
            }
        }

        return result;
    }

    /*
        Game-Platform Data
        ============
        - Game ID
            - Game ID MUST BE VALID
        - Platform
            - Platform CANNOT BE NULL
        - NO DUPLICATE Platform FOR GAMES
     */

    public List<GamePlatform> findPlatformsByGameId(int gameId) {
        return gamePlatformRepository.findByGameId(gameId);
    }

    public Result<GamePlatform> add(GamePlatform gamePlatform) {
        Result<GamePlatform> result = validate(gamePlatform);

        if(!result.isSuccess()) {
            return result;
        }

        if(!gamePlatformRepository.add(gamePlatform)) {
            result.addMessages("Platform Not Added", ResultType.INVALID);
        }

        return result;
    }

    public boolean deletePlatformById(int gameId, int platformId) {
        return gamePlatformRepository.deleteById(gameId, platformId);
    }

    private Result<GamePlatform> validate(GamePlatform gamePlatform) {
        Result<GamePlatform> result = new Result<>();

        if(gamePlatform == null) {
            result.addMessages("GamePlatform CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if (gamePlatform.getGameId() <= 0) {
            result.addMessages("Valid Game ID is REQUIRED", ResultType.INVALID);
        } else if (repository.findById(gamePlatform.getGameId()) == null) {
            result.addMessages("Game does not exist", ResultType.INVALID);
        }

        if(gamePlatform.getPlatform() == null) {
            result.addMessages("Platform CANNOT BE NULL", ResultType.INVALID);
        }

        List<GamePlatform> platforms = gamePlatformRepository.findByGameId(gamePlatform.getGameId());
        for(GamePlatform gp : platforms) {
            if(gp.getPlatform().getPlatformId() == gamePlatform.getPlatform().getPlatformId()) {
                result.addMessages("This Platform already is associated with this game", ResultType.INVALID);
                return result;
            }
        }

        return result;
    }
}
