# View-Tique Project Plan

## High-Level Requirements

This web app will serve as a hub for game user/critic reviews, and allow users to wishlist games and track games. As well as access information about each game and comment and like/dislike other user/critic reviews.

- **Add a Review**: Users an Add a review to a game.
- **Update a Review**: Users can update their review if the update is made with 24 hours of the posted date and time.
- **Remove a Review**: Users can delete their reviews at any time.

## Game Data

- Game data will be fetched from an external API, and updated once a week.

### Data

each game will include the following

- **Game Title**: The name of the game.
- **Game Description**: Brief description of the game
- **Platforms**: The systems the game can be played on.
- **Release Date**: The release date of the game.
- **Genres**: Genres of the game (Action, Shooter, etc.)
- **Developer**: The developer of the game.
- **Total User Score**: The score from averaging the rating from all user reviews
- **Total Critic Score**: The score from averaging the rating from all critic reviews

### Validation

To maintain data integrity and ensure usability, the following validation rules should apply:

- **Game Title**: REQUIRED
- **Game Description**: REQUIRED
- **Platforms**: REQUIRED
- **Release Date**: REQUIRED, MUST BE IN THE PAST
- **Genres**: REQUIRED
- **Developer**: REQUIRED
- **Total User Score**: REQUIRED, MUST BE BETWEEN 0 - 100
- **Total Critic Score**: REQUIRED, MUST BE BETWEEN 0 - 100

## Review Data

### Data

each review will include the following data

- **Review Score**: Score given to the game by the user
- **Timestamp**: Time the Review was made.
- **Review Body**: The text of the review.
- **Likes**
- **Dislikes**

### Validation

To maintain data integrity and ensure usability, the following validation rules should apply:

- **Review Score**: REQUIRED
- **Timestamp**: MUST BE IN THE PRESENT OR PAST
- **Review Body**: REQUIRED
- **Likes**: MUST BE POSITIVE
- **Dislikes**: MUST BE POSITIVE

## Comment Data

### Data

each comment will include the following data

- **Comment Body**: The text of the comment
- **Timestamp**: DAte and time the comment was made.

### Validation

To maintain data integrity and ensure usability, the following validation rules should apply:

- **Comment Body**: REQUIRED
- **Timestamp**: MUST BE IN THE PRESENT OR PAST

## Genre Data

### Data

each Genre will include the following data

- **Genre Name**: The name of the genre.

### Validation

To maintain data integrity and ensure usability, the following validation rules should apply:

- **Genre Name**: REQUIRED

## Platform Data

### Data
 
each platform will have the following data

- **Platform Name**: The platform name

### Validation

To maintain data integrity and ensure usability, the following validation rules should apply:

- **Platform Name**: REQUIRED

## Developer Data

### Data

each Developer will have the following data

- **Developer Name**

### Validation

To maintain data integrity and ensure usability, the following validation rules should apply:

- **Developer Name**: REQUIRED

## Wishlist

### Data

each wishlist will have the following data

- **Game ID**
- **User ID**

### Validation

To maintain data integrity and ensure usability, the following validation rules should apply:

- **Game ID**: REQUIRED
- **User ID**: REQUIRED


# Tables

- **Game Table**
- **Review Table**: Contains score for game reviewed, likes, and dislikes
- **Comment Table**
- **Platform Table**
- **Genre Table**
- **Game_Genre Table**
- **Game_Platform Table**
- **Developer Table**
- **Wishlist Table**



```
src
├───main
│   └───java
│       └───learn
│           └───review-tique
│               │   App.java                      -- app entry point
│               │
│               ├───controllers
│               │       GlobalExceptionHandler.java
│               │       ErrorResponse.java
│               │       GameController.java
│               │       ReviewController.java
|               |       CommentController.java
|               |       GenreController.java
|               |       PlatformController.java
|               |       DeveloperController.java
|               |       WishlistController.java  
│               │       GameGenreController.java
│               │       GamePlatformController.java
│               ├───data
│               │       DataException.java        -- data layer custom exception
│               │       GameJdbctemplateRepository.java  -- concrete repository
│               │       GameRepository.java      -- repository interface
│               │       ReviewJdbctemplateRepository.java  -- concrete repository
│               │       ReviewRepository.java      -- repository interface
│               │       CommentJdbctemplateRepository.java  -- concrete repository
│               │       CommentRepository.java      -- repository interface
│               │       GenreJdbctemplateRepository.java  -- concrete repository
│               │       GenreRepository.java      -- repository interface
│               │       PlatformJdbctemplateRepository.java  -- concrete repository
│               │       PlatformRepository.java      -- repository interface
│               │       DeveloperJdbctemplateRepository.java  -- concrete repository
│               │       DeveloperRepository.java      -- repository interface
│               │       WishlistJdbctemplateRepository.java  -- concrete repository
│               │       WishlistRepository.java      -- repository interface
│               │       GameGenreJdbctemplateRepository.java  -- concrete repository
│               │       GameGenreRepository.java      -- repository interface
│               │       GamePlatformJdbctemplateRepository.java  -- concrete repository
│               │       GamePlatformRepository.java      -- repository interface
│               │
│               ├───domain
│               │       Result.java          -- domain result for handling success/failure
│               │       ResultType.java         -- enum value for Result
│               │       GameService.java         -- game validation/rules
│               │       ReviewService.java         -- review validation/rules
│               │       CommentService.java         -- comment validation/rules
│               │       GenreService.java         -- Genre validation/rules
│               │       PlatformService.java         -- Platform validation/rules
│               │       DeveloperService.java         -- developer validation/rules
│               │       WishlistService.java         -- wishlist validation/rules
│               │       GameGenreService.java         -- GameGenre validation/rules
│               │       GamePlatformService.java         -- GamePlatform validation/rules
│               │
│               ├───models
│               │       Game.java	-- game model
│               │       Review.java     -- review model
│               │       User.java     -- user model
│               │       Comment.java     -- comment model
│               │       Genre.java     -- genre model
│               │       Platform.java      -- Platform model
│               │       Developer.java      -- developer model
│               │       Wishlist.java      -- wishlist model
│               │       GameGenre.java      -- GameGenre model
│               │       GamePlatform.java      -- GamePlatform model
│               │
|               └───security
│                       AppUserService.java         -- user validation/rules
│                       JwtConverter.java         
│                       JwtRequestFilter.java         
│                       SecurityConfig.java         
|
|
└───test
    └───java
        └───learn
            └───review-tique
                ├───controller
│               │       GlobalExceptionHandlerTest.java
│               │       GameControllerTest.java
│               │       ReviewControllerTest.java
|               |       CommentControllerTest.java
|               |       GenreControllerTest.java
|               |       PlatformControllerTest.java
|               |       DeveloperControllerTest.java
|               |       WishlistControllerTest.java  
│               │       GameGenreControllerTest.java
│               │       GamePlatformControllerTest.java
                │
                ├───domain
│               │       GameServiceTest.java         
│               │       ReviewServiceTest.java        
│               │       CommentServiceTest.java         
│               │       GenreServiceTest.java         
│               │       PlatformServiceTest.java         
│               │       DeveloperServiceTest.java         
│               │       WishlistServiceTest.java         
│               │       GameGenreServiceTest.java        
│               │       GamePlatformServiceTest.java        
                └───data
│               │       GameJdbctemplateRepositoryTest.java  
│               │       ReviewJdbctemplateRepositoryTest.java  
│               │       CommentJdbctemplateRepositoryTest.java  
│               │       GenreJdbctemplateRepositoryTest.java  
│               │       PlatformJdbctemplateRepositoryTest.java  
│               │       DeveloperJdbctemplateRepositoryTest.java  
│               │       WishlistJdbctemplateRepositoryTest.java  
│               │       GameGenreJdbctemplateRepositoryTest.java  
│               │       GamePlatformJdbctemplateRepositoryTest.java  
```



# Class Details

### APP

- `public static void main(String[])` -- instantiate all required classes with valid arguments, dependency injection. run controller

### data.DataException

Custom Data Exception

- `public DataException(String, Throwable)` -- constructor, throwable is root cause


## Models

### model.Game
- `private int gameId`
- `private String title`
- `private String description`
- `private List<Platform> platforms`
- `private LocalDate releaseDate`
- `private Developer developer`
- `private List<Genre> genres`
- `private double totalUserScore`
- `private double totalCriticScore`

### model.Review
- `private int reviewId`
- `private double score`
- `private Timestamp timestamp`
- `private String reviewBody`
- `private int likes`
- `private int dislikes`

### model.Comment
- `private int commentId`
- `private String commentBody`
- `private Timestamp timestamp`

### model.Genre
- `private int genreId`
- `private String genreName`

### model.Platform
- `private int platformId`
- `private String platformName`

### model.Developer
- `private int developerId`
- `private String developerName`

### model.GameGenre
- `private int gameId`
- `private Genre genre`

### model.GamePlatform
- `private int gameId`
- `private Platform platform`

### model.Wishlist
- `private int wishlistId`
- `private int gameId`
- `private int userId`


## Data 

### data.GameJdbctemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `public List<Game> findAll()` -- finds all games
- `public Game findById(int gameId)` -- finds game by id
- `public List<Game> findByTitle(String title)` -- finds a game by its title
- `public List<Game> findByPlatform(List<Integer> platformIds)`
- `public List<Game> findByGenre(List<Integer> genreIds)`
- `public List<Game> findByDeveloper(List<Integer> devIds)`
- `public Game add(Game game)`
- `public boolean update(Game game)`
- `public boolean delete(int gameId)`

### data.ReviewJdbcTemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `public List<Review> findAll()` -- finds all reviews
- `public Review findById(int reviewId)` -- finds review by id
- `public List<Review> findByUserId(int userId)` -- finds reviews by user
- `public List<Review> findByGameId(int gameId)` -- finds reviews by game
- `public Review add(Review review)`
- `public boolean update(Review review)`
- `public boolean delete(int reviewId)`

### data.CommentJdbcTemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `public List<Comment> findAll()` -- finds all comments
- `public Comment findById(int commentId)` -- finds comment by id
- `public List<Comment> findByUserId(int userId)` -- finds comments by user
- `public List<Comment> findByReviewId(int reviewId)` -- find comments by review
- `public Comment add(Comment comment)`
- `public boolean update(Comment comment)`
- `public boolean delete(int commentId)`

### data.GenreJdbcTemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `public List<Genre> findAll()` -- finds all Genres
- `public Genre findById()` -- finds Genre by the id
- `public Genre add(Genre)` -- adds an Genre
- `public boolean update(Genre)` -- updates an Genre
- `public boolean deleteById(int)` -- deletes an Genre

### data.PlatformJdbcTemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `public List<Platform> findAll()` -- finds all Platforms
- `public Platform findById()` -- finds Platform by the id
- `public Platform add(Platform)` -- adds an Platform
- `public boolean update(Platform)` -- updates an Platform
- `public boolean deleteById(int)` -- deletes an Platform

### data.DeveloperJdbcTemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `public List<Developer> findAll()` -- finds all Developers
- `public Developer findById()` -- finds Developer by the id
- `public Developer add(Developer)` -- adds an Developer
- `public boolean update(Developer)` -- updates an Developer
- `public boolean deleteById(int)` -- deletes an Developer

### data.GameGenreJdbcTemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `public List<GameGenre> findByGameId(int)`
- `public boolean add(GameGenre)`
- `public boolean delete(int gameId, int genreId)`

### data.GamePlatformJdbcTemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `private GameGenre findByIds(int, int)`
- `public List<GamePlatform> findByGameId(int)`
- `public boolean add(GamePlatform)`
- `public boolean delete(int gameId, int platformId)`

### data.WishlistJdbcTemplateRepository

- `private final JdbcTemplate jdbcTemplate`
- `public List<Wishlist> findAll()` -- finds all Wishlists
- `public Wishlist findById(int)` -- finds Wishlist by the id
- `public List<Wishlist> findByUserId(int)` -- finds wishlist items based on user
- `public Wishlist add(Wishlist)` -- adds an Wishlist
- `public boolean update(Wishlist)` -- updates an Wishlist
- `public boolean deleteById(int)` -- deletes an Wishlist

## Domain

### domain.Result

- `private ArrayList<String> messages` -- error messages
- `private ResultType type` -- result type
- `private T payload` -- payload
- `public ResultType getType()` -- type getter
- `public boolean isSuccess()` -- calculated getter, true if no error messages
- `public T getPayload()` -- payload getter
- `public void setPayload(Payload)` -- payload setter
- `public List<String> getMessages()` -- messages getter, create a new list
- `public void addMessage(String, ResultType)` -- adds an error message to messages

### domain.GameService

- `private GameRepository repository` -- required data dependency
- `public GameService(GameRepository)` -- constructor
- `public List<Game> findAll()` -- pass-through to repository
- `public Game findById(int gameId)` -- pass-through to repository
- `public List<Game> findByTitle(String)` -- pass-through to repository
- `public List<Game> findByPlatform(List<Integer>)` -- pass-through to repository
- `public List<Game> findByGenre(List<Integer>)` -- pass-through to repository
- `public List<Game> findByDeveloper(List<Integer>)` -- pass-through to repository
- `public Result add(Game)` -- validate, then add via repository
- `public Result update(Game)` -- validate, then add via repository
- `public boolean delete(int gameId)` -- pass-through to repository
- `private Result validate(Game)` -- general-purpose validation routine

### domain.ReviewService

- `private ReviewRepository repository` -- required data dependency
- `public ReviewService(ReviewRepository)` -- constructor
- `public Review findById(int)` -- pass-through to repository
- `public List<Review> findByUserId(int)` -- pass-through to repository
- `public List<Review> findByGameId(Int)` -- pass-through to repository
- `public Result add(Review)` -- validate, then add via repository
- `public Result update(Review)` -- validate, then update via repository
- `public Result deleteById(int)` -- pass-through to repository
- `private Result validate(Review)` -- general-purpose validation routine

### domain.CommentService

- `private CommentRepository repository` -- required data dependency
- `public CommentService(CommentRepository)` -- constructor
- `public Comment findById(int)` -- pass-through to repository
- `public List<Comment> findByUserId(int)` -- pass-through to repository
- `public List<Comment> findByReviewId(Int)` -- pass-through to repository
- `public Result add(Comment)` -- validate, then add via repository
- `public Result update(Comment)` -- validate, then update via repository
- `public Result deleteById(int)` -- pass-through to repository
- `private Result validate(Comment)` -- general-purpose validation routine

### domain.GenreService

- `private GenreRepository repository` -- required data dependency
- `public GenreService(GenreRepository)` -- constructor
- `public List<Genre> findAll()` -- pass-through to repository
- `public Genre findById(int)` -- pass-through to repository
- `public Result add(Genre)` -- validate, then add via repository
- `public Result update(Genre)` -- validate, then update via repository
- `public Result deleteById(int)` -- pass-through to repository
- `private Result validate(Genre)` -- general-purpose validation routine

### domain.PlatformService

- `private PlatformRepository repository` -- required data dependency
- `public PlatformService(PlatformRepository)` -- constructor
- `public List<Platform> findAll()` -- pass-through to repository
- `public Platform findById(int)` -- pass-through to repository
- `public Result add(Platform)` -- validate, then add via repository
- `public Result update(Platform)` -- validate, then update via repository
- `public Result deleteById(int)` -- pass-through to repository
- `private Result validate(Platform)` -- general-purpose validation routine

### domain.DeveloperService

- `private DeveloperRepository repository` -- required data dependency
- `public DeveloperService(DeveloperRepository)` -- constructor
- `public List<Developer> findAll()` -- pass-through to repository
- `public Developer findById(int)` -- pass-through to repository
- `public Result add(Developer)` -- validate, then add via repository
- `public Result update(Developer)` -- validate, then update via repository
- `public Result deleteById(int)` -- pass-through to repository
- `private Result validate(Developer)` -- general-purpose validation routine

### domain.WishlistService

- `private WishlistRepository repository` -- required data dependency
- `public WishlistService(WishlistRepository)` -- constructor
- `public List<Wishlist> findAll()` -- pass-through to repository
- `public Wishlist findById(int)` -- pass-through to repository
- `public List<Wishlist> findByUserId(int)` -- pass-through to repository
- `public Result add(Wishlist)` -- validate, then add via repository
- `public Result update(Wishlist)` -- validate, then update via repository
- `public Result deleteById(int)` -- pass-through to repository
- `private Result validate(Wishlist)` -- general-purpose validation routine

### domain.GameGenreService

- `private GameGenreRepository repository`
- `public GameGenreService(GameGenreRepository)`
- `public List<GameGenre> findAll()`
- `public GameGenre findByIds(int, int)`
- `public List<GameGenre> findByGameId(int)`
- `public Result add(GameGenre)`
- `public Result update(GameGenre)`
- `public boolean delete(int)`

### domain.GamePlatformService

- `private GamePlatformRepository repository`
- `public GamePlatformService(GamePlatformRepository)`
- `public List<GamePlatform> findAll()`
- `public GamePlatform findByIds(int, int)`
- `public List<GamePlatform> findByGameId(int)`
- `public Result add(GamePlatform)`
- `public Result update(GamePlatform)`
- `public boolean delete(int)`


## Controllers

### controller.GameController

- `private GameService service`
- `public GameController(GameService)`
- `public ResponseEntity<List<Game>> findAll()`
- `public ResponseEntity<Game> findById(int gameId)` -- pass-through to repository
- `public ResponseEntity<List<Game>> findByTitle(String)` -- pass-through to repository
- `public ResponseEntity<List<Game>> findByPlatform(List<Integer>)` -- pass-through to repository
- `public ResponseEntity<List<Game>> findByGenre(List<Integer>)` -- pass-through to repository
- `public ResponseEntity<List<Game>> findByDeveloper(List<Integer>)` -- pass-through to repository
- `public ResponseEntity<Object> add(Game)` -- validate, then add via repository
- `public ResponseEntity<Object> update(Game)` -- validate, then add via repository
- `public ResponseEntity<Object> delete(int gameId)` -- pass-through to repository


### controller.ReviewController

- `private GameService service`
- `public GameController(GameService)`
- `public List<Review> findById(int)` -- pass-through to repository
- `public ResponseEntity<List<Review>> findByUserId(int)` -- pass-through to repository
- `public ResponseEntity<List<Review>> findByGameId(Int)` -- pass-through to repository
- `public ResponseEntity<Object> add(Review)` -- validate, then add via repository
- `public ResponseEntity<Object> update(Review)` -- validate, then update via repository
- `public ResponseEntity<Object> deleteById(int)` -- pass-through to repository

### controller.CommentController

- `public Comment findById(int)` -- pass-through to repository
- `public ResponseEntity<List<Comment>> findByUserId(int)` -- pass-through to repository
- `public ResponseEntity<List<Comment>> findByReviewId(Int)` -- pass-through to repository
- `public ResponseEntity<Object> add(Comment)` -- validate, then add via repository
- `public ResponseEntity<Object> update(Comment)` -- validate, then update via repository
- `public ResponseEntity<Object> deleteById(int)` -- pass-through to repository

### controller.GenreController

- `public List<Genre> findAll()` -- pass-through to repository
- `public Genre findById(int)` -- pass-through to repository
- `public ResponseEntity<Object> add(Genre)` -- validate, then add via repository
- `public ResponseEntity<Object> update(Genre)` -- validate, then update via repository
- `public ResponseEntity<Object> deleteById(int)` -- pass-through to repository

### controller.PlatformController

- `public List<Platform> findAll()` -- pass-through to repository
- `public Platform findById(int)` -- pass-through to repository
- `public ResponseEntity<Object> add(Platform)` -- validate, then add via repository
- `public ResponseEntity<Object> update(Platform)` -- validate, then update via repository
- `public ResponseEntity<Object> deleteById(int)` -- pass-through to repository

### controller.DeveloperController

- `public List<Developer> findAll()` -- pass-through to repository
- `public Developer findById(int)` -- pass-through to repository
- `public ResponseEntity<Object> add(Developer)` -- validate, then add via repository
- `public ResponseEntity<Object> update(Developer)` -- validate, then update via repository
- `public ResponseEntity<Object> deleteById(int)` -- pass-through to repository

### controller.WishlistController

- `public List<Wishlist> findAll()` -- pass-through to repository
- `public Wishlist findById(int)` -- pass-through to repository
- `public ResponseEntity<List<Wishlist>> findByUserId(int)` -- pass-through to repository
- `public ResponseEntity<Object> add(Wishlist)` -- validate, then add via repository
- `public ResponseEntity<Object> update(Wishlist)` -- validate, then update via repository
- `public ResponseEntity<Object> deleteById(int)` -- pass-through to repository

### controller.GameGenreController

- `public List<GameGenre> findAll()`
- `public GameGenre findByIds(int, int)`
- `public List<GameGenre> findByGameId(int)`
- `public ResponseEntity<Object> add(GameGenre)`
- `public ResponseEntity<Object> update(GameGenre)`
- `public ResponseEntity<Object> delete(int)`

### controller.GamePlatformController

- `public List<GamePlatform> findAll()`
- `public GamePlatform findByIds(int, int)`
- `public ResponseEntity<List<GamePlatform>> findByGameId(int)`
- `public ResponseEntity<Object> add(GamePlatform)`
- `public ResponseEntity<Object> update(GamePlatform)`
- `public ResponseEntity<Object> delete(int)`
