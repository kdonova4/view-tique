package learn.review_tique.domain;

import learn.review_tique.data.PlatformRepository;
import learn.review_tique.models.Platform;
import org.springframework.stereotype.Service;

import java.util.List;

/*
        Platform Data
        ============
        - Platform Name
            - Platform Name CANNOT BE NULL OR BLANK


        public List<Platform> findAll()
            // return repo findAll() method

        public Platform findById(int platformId)
            // return repo findById() method

        public List<Platform> searchByName(String platformId)
            // return repo searchByName() method

        public Result<Platform> add(Platform platform)
            // validate
            // if validate is not successful, then add message and type and return result
            // if ID is set, add message and type and return result
            // set Platform to repo.add() method
            // set payload to the returned Platform
            // return result

        public Result<Platform> update(Platform platform)
            // validate
            // if validate not successful, add message and type then return result
            // check if ID is not set, if yes then add message and type and return result
            // if repo call to update() method is unsuccessful add message and type
            // return result

        public boolean deleteById(int platformId)
            //return repo call to deleteById()

        private Result<Platform> validate(Platform platform)
            // new result
            // check if Platform is null, if it is then add message and type and return result
            // check if Platform Name is NULL or BLANK, if it is then add message and type

     */
@Service
public class PlatformService {

    private final PlatformRepository repository;

    public PlatformService(PlatformRepository repository) {
        this.repository = repository;
    }

    public List<Platform> findAll() {
        return repository.findAll();
    }

    public Platform findById(int platformId) {
        return repository.findById(platformId);
    }

    public List<Platform> searchByName(String platformName) {
        return repository.searchByName(platformName);
    }

    public Result<Platform> add(Platform platform) {
        Result<Platform> result = validate(platform);

        if(!result.isSuccess())
            return result;

        if(platform.getPlatformId() != 0) {
            result.addMessages("Platform ID CANNOT BE SET", ResultType.INVALID);
            return result;
        }

        platform = repository.add(platform);
        result.setPayload(platform);
        return result;
    }

    public Result<Platform> update(Platform platform) {
        Result<Platform> result = validate(platform);

        if(!result.isSuccess())
            return result;

        if(platform.getPlatformId() <= 0) {
            result.addMessages("Platform ID MUST BE SET", ResultType.INVALID);
            return result;
        }

        if(!repository.update(platform)) {
            result.addMessages(String.format("Platform ID %s NOT FOUND", platform.getPlatformId()), ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int platformId) {
        return repository.deleteById(platformId);
    }

    private Result<Platform> validate(Platform platform) {
        Result<Platform> result = new Result<>();

        if(platform == null) {
            result.addMessages("Platform CANNOT BE NULL", ResultType.INVALID);
            return result;
        }

        if(platform.getPlatformName() == null || platform.getPlatformName().isBlank()) {
            result.addMessages("Platform Name is REQUIRED", ResultType.INVALID);
        }

        return result;
    }

}
