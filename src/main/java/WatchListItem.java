import java.util.Map;

public class WatchListItem extends Entity{
    String userId;

    public WatchListItem(String userEmail, int _movieId){
        id = String.valueOf(_movieId);
        userId = userEmail;
    }

    public WatchListItem(Map<String, Object> args){
        this((String) args.get("userEmail"), (int) args.get("movieId"));
    }

    public Map<String, Object> getJsonMap() {
        return null;
    }
}
