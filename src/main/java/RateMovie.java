import java.util.Map;

public class RateMovie extends Entity{
    int movieId;
    int score;

    public RateMovie(String userEmail, int _movieId, int _score){
        id = userEmail;
        movieId = _movieId;
        score = _score;
    }

    public RateMovie(Map<String, Object> args){
        this((String) args.get("userEmail"), (int) args.get("movieId"), (int) args.get("score"));
    }

    public Map<String, Object> getJsonMap() {
        return null;
    }
}
