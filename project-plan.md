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


# Class Details

### APP

- `public static void main(String[])` -- instantiate all required classes with valid arguments, dependency injection. run controller

### data.DataException

Custom Data Exception

- `public DataException(String, Throwable)` – constructor, throwable is root cause


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

### data.ReviewJdbcTemplateRepository

### data.CommentJdbcTemplateRepository

### data.GenreJdbcTemplateRepository

### data.PlatformJdbcTemplateRepository

### data.DeveloperJdbcTemplateRepository

### data.GameGenreJdbcTemplateRepository

### data.GamePlatformJdbcTemplateRepository

### data.WIshlistJdbcTemplateRepository

## Domain

### domain.Result

### domain.GameService

### domain.ReviewService

### domain.CommentService

### domain.GenreService

### domain.PlatformService

### domain.DeveloperService

### domain.WishlistService

### domain.GameGenreService

### domain.GamePlatformService

## Controllers

### controller.GameService

### controller.ReviewService

### controller.CommentService

### controller.GenreService

### controller.PlatformService

### controller.DeveloperService

### controller.WishlistService

### controller.GameGenreService

### controller.GamePlatformService