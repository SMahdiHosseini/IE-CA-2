import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

public class Client extends Entity {
    Repository watchList;
    String password;
    String nickname;
    String name;
    Date birthDate;

    public Client(String _id, String _password, String _nickname, String _name, String _birthDate){
        id = _id;
        password = _password;
        nickname = _nickname;
        name = _name;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthDate = formatter.parse(_birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        watchList = new Repository();
    }

    public Client(Map<String, Object> args){
        this((String) args.get("email"), (String) args.get("password"), (String) args.get("nickname"),
                (String) args.get("name"), (String) args.get("birthDate"));
    }

    public boolean addToWatchList(Map<String, Object> args){
        if(watchList.getById(String.valueOf(args.get("movieId"))) == null){
            watchList.add(new WatchListItem(args));
            return true;
        }
        return false;
    }

    public boolean removeFromWatchList(Map<String, Object> args){
        return watchList.remove(String.valueOf(args.get("movieId")));
    }

    public boolean legalToWatch(int ageLimit){
        Date now = new Date(System.currentTimeMillis());
        return now.getYear() - birthDate.getYear() >= ageLimit;
    }

    public Map<String, Object> getJsonMap(){
        return null;
    }

}
