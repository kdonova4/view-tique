package learn.review_tique.domain;

import learn.review_tique.data.DeveloperRepository;
import learn.review_tique.models.Developer;
import org.springframework.stereotype.Service;

import java.util.List;


/*
        Developer Data
        ============
        - Developer Name
            - Name CANNOT BE NULL OR BLANK

        public List<Developer> findAll()
            // return repo findAll() method

        public Developer findById(int developerId)
            // return repo findById() method

        public List<Developer> searchByName(String developerName)
            // return repo searchByName() method

        public Result<Developer> add(Developer developer)
            // validate
            // if validate is not successful, then add message and type and return result
            // if ID is set, add message and type and return result
            // set developer to repo.add() method
            // set payload to the returned developer
            // return result

        public Result<Developer> update(Developer developer)
            // validate
            // if validate not successful, add message and type then return result
            // check if ID is not set, if yes then add message and type and return result
            // if repo call to update() method is unsuccessful add message and type
            // return result

        public boolean deleteById(int developerId)
            //return repo call to deleteById()

        private Result<Developer> validate(Developer developer)
            // new result
            // check if developer is null, if it is then add message and type and return result
            // check if Developer Name is NULL or BLANK, if it is then add message and type

     */


@Service
public class DeveloperService {

    private final DeveloperRepository repository;


    public DeveloperService(DeveloperRepository repository) {
        this.repository = repository;
    }

    public List<Developer> findAll() {
        return repository.findAll();
    }

    public Developer findById(int developerId) {
        return repository.findById(developerId);
    }

    public Developer findByName(String developerName) {
        return repository.findByName(developerName);
    }

    public List<Developer> searchByName(String developerName) {
        return repository.searchByName(developerName);
    }

    public Result<Developer> add(Developer developer) {
        Result<Developer> result = validate(developer);

        if(!result.isSuccess()) {
            return result;
        }

        if(developer.getDeveloperId() != 0) {
            result.addMessages("developerId CANNOT BE SET for `add` operation", ResultType.INVALID);
            return result;
        }

        developer = repository.add(developer);
        result.setPayload(developer);
        return result;
    }

    public Result<Developer> update(Developer developer) {
        Result<Developer> result = validate(developer);

        if(!result.isSuccess())
            return result;

        if(developer.getDeveloperId() <= 0) {
            result.addMessages("developerId MUST BE SET for `update` operation", ResultType.INVALID);
            return result;
        }

        if(!repository.update(developer)) {
            result.addMessages(String.format("Developer %s NOT FOUND", developer.getDeveloperId()), ResultType.NOT_FOUND);
            return result;
        }

        return result;
    }

    public boolean deleteById(int developerId) {
        return repository.deleteById(developerId);
    }

    private Result<Developer> validate(Developer developer) {
        Result<Developer> result = new Result<>();

        if(developer == null) {
            result.addMessages("Developer CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(developer.getDeveloperName() == null || developer.getDeveloperName().isBlank()) {
            result.addMessages("Developer Name is REQUIRED", ResultType.INVALID);
        }

        return result;
    }

}
