package learn.review_tique.data;

import learn.review_tique.data.mappers.GameMapper;
import learn.review_tique.models.Game;
import learn.review_tique.models.Genre;
import learn.review_tique.models.Platform;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class GameJdbcTemplateRepository implements GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Game> findAll() {
        final String sql = "select g.game_id, g.title, g.game_description, g.release_date, g.avg_user_score, g.avg_critic_score, "
                + "g.user_review_count, g.critic_review_count, g.cover, g.developer_id, d.developer_name"
                + " from game g"
                + " join developer d on g.developer_id = d.developer_id;";

        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game findById(int gameId) {
        final String sql = "select g.game_id, g.title, g.game_description, g.release_date, g.avg_user_score, g.avg_critic_score, "
                + "g.user_review_count, g.critic_review_count, g.cover, g.developer_id, d.developer_name"
                + " from game g"
                + " join developer d on g.developer_id = d.developer_id"
                + " where game_id = ?;";

        Game result =  jdbcTemplate.query(sql, new GameMapper(), gameId).stream().findFirst().orElse(null);

        if(result != null) {
            addGenres(result);
            addPlatforms(result);
        }

        return result;
    }

    @Override
    public List<Game> findTopNGamesByGenre(int genreId, int limit) {
        final String sql = "select g.game_id, g.title, g.game_description, g.release_date, g.avg_user_score, g.avg_critic_score,"
                + " g.user_review_count, g.critic_review_count, g.cover, g.developer_id, d.developer_name"
                + " from game g"
                + " join developer d on g.developer_id = d.developer_id"
                + " join game_genre gg on g.game_id = gg.game_id"
                + " where gg.genre_id = ?"
                + " order by g.avg_critic_score desc"
                + " limit ?";

        return jdbcTemplate.query(sql, new GameMapper(), genreId, limit);
    }

    @Override
    public List<Game> searchGame(String gameName, int[] genreIds, int[] platformIds, Integer developerId) {

        if ((gameName == null || gameName.isEmpty()) &&
                (genreIds == null || genreIds.length == 0) &&
                (platformIds == null || platformIds.length == 0) &&
                (developerId == null)) {
            return Collections.emptyList();
        }
        String fullTextParam = gameName + "*";

        StringBuilder sql = new StringBuilder(
                "select distinct g.game_id, g.title, g.game_description, g.release_date, g.avg_user_score, g.avg_critic_score, "
                        + "g.user_review_count, g.critic_review_count, g.cover, g.developer_id, d.developer_name, "
                        + "MATCH(g.title) AGAINST(? IN BOOLEAN MODE) as relevance_score "
                        + "from game g "
                        + "join developer d on g.developer_id = d.developer_id "
        );
        List<Object> params = new ArrayList<>();
        params.add(gameName);
        if (gameName != null && !gameName.isBlank()) {
            sql.append("and MATCH(g.title) AGAINST(? IN BOOLEAN MODE) ");
            params.add(fullTextParam);
        }

        if (genreIds != null && genreIds.length > 0)
            sql.append("join game_genre gg on g.game_id = gg.game_id ");
        if (platformIds != null && platformIds.length > 0)
            sql.append("join game_platform gp on g.game_id = gp.game_id ");

        sql.append("where 1 = 1 ");





        if (genreIds != null && genreIds.length > 0) {
            sql.append("and gg.genre_id in (")
                    .append(String.join(", ", Collections.nCopies(genreIds.length, "?")))
                    .append(") ");
            for (int genreId : genreIds) {
                params.add(genreId);
            }
        }

        if (platformIds != null && platformIds.length > 0) {
            sql.append("and gp.platform_id in (")
                    .append(String.join(", ", Collections.nCopies(platformIds.length, "?")))
                    .append(") ");
            for (int platformId : platformIds) {
                params.add(platformId);
            }
        }

        if (developerId != null) {
            sql.append("and g.developer_id = ? ");
            params.add(developerId);
        }
        sql.append("ORDER BY (g.title = ?) DESC, relevance_score DESC");
        params.add(gameName);


        return jdbcTemplate.query(sql.toString(), new GameMapper(), params.toArray());
    }

    @Override
    public Game add(Game game) {
        final String sql = "insert into game (title, game_description, release_date, avg_user_score, avg_critic_score," +
                " user_review_count, critic_review_count, cover, developer_id)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getDescription());
            ps.setDate(3, Date.valueOf(game.getReleaseDate()));
            ps.setDouble(4, game.getAvgUserScore());
            ps.setDouble(5, game.getAvgCriticScore());
            ps.setInt(6, game.getUserReviewCount());
            ps.setInt(7, game.getCriticReviewCount());
            ps.setInt(8, game.getDeveloper().getDeveloperId());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0)
            return null;

        game.setGameId(keyHolder.getKey().intValue());
        return game;
    }

    @Override
    public boolean update(Game game) {
        final String sql = "update game set " +
                "title = ?,"
                + " game_description = ?,"
                + " release_date = ?,"
                + " avg_user_score = ?,"
                + " avg_critic_score = ?,"
                + " user_review_count = ?,"
                + " critic_review_count = ?,"
                + " developer_id = ?"
                + " where game_id = ?;";

        return jdbcTemplate.update(sql, game.getTitle(),
                game.getDescription(),
                Date.valueOf(game.getReleaseDate()),
                game.getAvgUserScore(),
                game.getAvgCriticScore(),
                game.getUserReviewCount(),
                game.getCriticReviewCount(),
                game.getDeveloper().getDeveloperId(),
                game.getGameId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int gameId) {
        final String gameGenreSql = "delete from game_genre where game_id = ?;";
        final String gamePlatformSql = "delete from game_platform where game_id = ?;";
        final String wishlistSql = "delete from wishlist where game_id = ?";
        final String reviewReactionSql = "delete from review_reaction where review_id in (select review_id from review where game_id = ?);";
        final String reviewSql = "delete from review where game_id = ?;";

        jdbcTemplate.update(gameGenreSql, gameId);
        jdbcTemplate.update(gamePlatformSql, gameId);
        jdbcTemplate.update(wishlistSql, gameId);
        jdbcTemplate.update(reviewReactionSql, gameId);
        jdbcTemplate.update(reviewSql, gameId);


        final String sql = "delete from game where game_id = ?;";

        return jdbcTemplate.update(sql, gameId) > 0;
    }

    private void addGenres(Game game) {
        final String sql = "select g.genre_id, g.genre_name" +
                " from genre g" +
                " join game_genre gg on g.genre_id = gg.genre_id" +
                " where gg.game_id = ?;";

        var genres = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setGenreId(rs.getInt("genre_id"));
            genre.setGenreName(rs.getString("genre_name"));
            return genre;
        }, game.getGameId());

        game.setGenres(genres);
    }

    private void addPlatforms(Game game) {
        final String sql = "select p.platform_id, p.platform_name" +
                " from platform p" +
                " join game_platform gp on p.platform_id = gp.platform_id" +
                " where gp.game_id = ?;";

        var platforms = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Platform platform = new Platform();
            platform.setPlatformId(rs.getInt("platform_id"));
            platform.setPlatformName(rs.getString("platform_name"));
            return platform;
        }, game.getGameId());

        game.setPlatforms(platforms);
    }
}
