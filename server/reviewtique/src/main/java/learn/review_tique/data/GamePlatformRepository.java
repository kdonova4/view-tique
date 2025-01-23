package learn.review_tique.data;


import learn.review_tique.models.GamePlatform;

import java.util.List;

public interface GamePlatformRepository {

    boolean add(GamePlatform gamePlatform);

    List<GamePlatform> findByGameId(int gameId);

    boolean deleteById(int gameId, int platformId);
}
