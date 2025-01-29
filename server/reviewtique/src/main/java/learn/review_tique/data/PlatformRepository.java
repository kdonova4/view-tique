package learn.review_tique.data;

import learn.review_tique.models.Platform;

import java.util.List;

public interface PlatformRepository {

    List<Platform> findAll();

    Platform findById(int platformId);

    List<Platform> searcByName(String platformName);

    Platform add(Platform platform);

    boolean update(Platform platform);

    boolean deleteById(int platformId);
}
