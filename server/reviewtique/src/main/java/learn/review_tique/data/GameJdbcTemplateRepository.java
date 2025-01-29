package learn.review_tique.data;

import learn.review_tique.data.mappers.GameMapper;
import learn.review_tique.models.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
        final String sql = "select game_id, title, game_description, release_date, avg_user_score, avg_critic_score, "
                + "user_review_count, critic_review_count, developer_id"
                + " from game;";

        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game findById(int gameId) {
        final String sql = "select game_id, title, game_description, release_date, avg_user_score, avg_critic_score, "
                + "user_review_count, critic_review_count, developer_id"
                + " from game where game_id = ?;";

        return jdbcTemplate.query(sql, new GameMapper(), gameId).stream().findFirst().orElse(null);
    }

    @Override
    public List<Game> searchGame(String gameName, int[] genreIds, int[] platformIds, Integer developerId) {
        StringBuilder sql = new StringBuilder(
                "select distinct g.game_id, g.title, g.game_description, g.release_date, g.avg_user_score, g.avg_critic_score, "
                        + "g.user_review_count, g.critic_review_count, g.developer_id"
                        + " from game g "

        );

        if(genreIds != null && genreIds.length > 0)
            sql.append("join game_genre gg on g.game_id = gg.game_id ");
        if(platformIds != null && platformIds.length > 0)
            sql.append("join game_platform gp on g.game_id = gp.game_id ");

        sql.append("where 1 = 1");

        List<Object> params = new ArrayList<>();

        if(gameName != null && !gameName.isBlank()) {
            sql.append("and soundex(g.title) = soundex(?) ");
            params.add(gameName);
        }

        if(genreIds != null && genreIds.length > 0) {
            sql.append("and gg.genre_id in (")
                    .append(String.join(", ", Collections.nCopies(genreIds.length, "?")))
                    .append(") ");
            for(int genreId : genreIds) {
                params.add(genreId);
            }

            params.add(genreIds.length);
        }

        if(platformIds != null && platformIds.length > 0) {
            sql.append("and gp.platform_id in (")
                    .append(String.join(", ", Collections.nCopies(platformIds.length, "?")))
                    .append(") ");
            for(int platformId : platformIds) {
                params.add(platformId);
            }
        }

        if(developerId != null) {
            sql.append("and g.developer_id = ? ");
            params.add(developerId);
        }

        return jdbcTemplate.query(sql.toString(), new GameMapper(), params.toArray());
    }

    @Override
    public Game add(Game game) {
        final String sql = "insert into game (title, game_description, release_date, avg_user_score, avg_critic_score," +
                " user_review_count, critic_review_count, developer_id)"
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
    public boolean deleteById(int gameId) {
        final String sql = "delete from game where game_id = ?;";

        return jdbcTemplate.update(sql, gameId) > 0;
    }
}
