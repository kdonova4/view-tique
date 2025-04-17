package learn.review_tique.data;

import learn.review_tique.models.Developer;

import java.util.List;

public interface DeveloperRepository {

    List<Developer> findAll();

    Developer findById(int developerId);

    Developer findByName (String developerName);

    Developer add(Developer developer);

    List<Developer> searchByName(String developerName);

    boolean update(Developer developer);

    boolean deleteById(int developerId);
}
