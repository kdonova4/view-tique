package learn.review_tique.models;

public class Developer {

    private int developerId;
    private String developerName;

    public Developer() {

    }

    public Developer(int developerId, String developerName) {
        this.developerId = developerId;
        this.developerName = developerName;
    }

    public int getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(int developerId) {
        this.developerId = developerId;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }
}
