import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Movie extends Entity {
    ArrayList<Object> comments;
    Repository rates;
    String name;
    String summary;
    Date releaseDate;
    String releaseDateString;
    String director;
    ArrayList<String> writers;
    ArrayList<String> genres;
    ArrayList<Integer> castIds;
    ArrayList<Object> castJsons;
    double imdbRate;
    String duration;
    int ageLimit;
    int sumRating;
    int ratersCount;

    public Movie(String _id, String _name, String _summary, String _releaseDate, String _director, ArrayList<String> _writers,
                    ArrayList<String> _genres, ArrayList<Integer> _cast, ArrayList<Object> _castJsons, double _imdbRate, String _duration, int _ageLimit){
        id = _id;
        name = _name;
        summary = _summary;
        director = _director;
        writers = _writers;
        genres = _genres;
        castIds = _cast;
        castJsons = _castJsons;
        imdbRate = _imdbRate;
        duration = _duration;
        ageLimit = _ageLimit;
        sumRating = 0;
        ratersCount = 0;
        releaseDateString = _releaseDate;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            releaseDate = formatter.parse(_releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        comments = new ArrayList<>();
        rates = new Repository();
    }

    public Movie(Map<String, Object> args, ArrayList<Object> castJsons){
        this(String.valueOf(args.get("id")), (String) args.get("name"), (String) args.get("summary"), (String) args.get("releaseDate")
                , (String) args.get("director"), (ArrayList<String>) args.get("writers"), (ArrayList<String>) args.get("genres")
                , (ArrayList<Integer>) args.get("cast"), castJsons, (Double) args.get("imdbRate")
                , String.valueOf(args.get("duration")), (Integer) args.get("ageLimit"));
    }

    public boolean actedInThisMovie(Integer actor_id){
        for (Integer cast_id : castIds)
            if (cast_id == actor_id)
                return true;
        return false;
    }

    public Map<String, Object> getJsonMap(){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("movieId", Integer.parseInt(id));
        jsonMap.put("name", name);
        jsonMap.put("summary", summary);
        jsonMap.put("releaseDate", releaseDateString);
        jsonMap.put("director", director);
        jsonMap.put("writers", writers);
        jsonMap.put("imdb Rate", imdbRate);
        jsonMap.put("genres", genres);
        jsonMap.put("cast", castJsons);
        jsonMap.put("rating", ratersCount==0?null:getRating());
        jsonMap.put("duration", Integer.parseInt(duration));
        jsonMap.put("ageLimit", ageLimit);
        jsonMap.put("comments", comments);
        return jsonMap;
    }

    public Map<String, Object> getJsonMap_Id(){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("movieId", Integer.parseInt(id));
        jsonMap.put("name", name);
        jsonMap.put("summary", summary);
        jsonMap.put("releaseDate", releaseDateString);
        jsonMap.put("director", director);
        jsonMap.put("writers", writers);
        jsonMap.put("genres", genres);
        jsonMap.put("cast", castJsons);
        jsonMap.put("rating", ratersCount==0?null:getRating());
        jsonMap.put("duration", Integer.parseInt(duration));
        jsonMap.put("ageLimit", ageLimit);
        jsonMap.put("comments", comments);
        return jsonMap;
    }

    public Map<String, Object> getJsonForActor(){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("movieId", Integer.parseInt(id));
        jsonMap.put("name", name);
        jsonMap.put("imdb Rate", imdbRate);
        jsonMap.put("rating", ratersCount==0?null:getRating());
        return jsonMap;
    }

    public Map<String, Object> getJsonMap_genre(){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("movieId", Integer.parseInt(id));
        jsonMap.put("name", name);
        jsonMap.put("director", director);
        jsonMap.put("genres", genres);
        jsonMap.put("imdb Rate", imdbRate);
        jsonMap.put("duration", duration);
        jsonMap.put("rating", ratersCount==0?null:getRating());
        jsonMap.put("releaseDate", releaseDateString);
        return jsonMap;
    }

    public double getRating(){
        if(ratersCount == 0)
            return 0;
        double rating = (double) sumRating/ratersCount;
        DecimalFormat numberFormat = new DecimalFormat("#.#");
        return Double.parseDouble(numberFormat.format(rating));
    }

    public void updateComment(ArrayList<Object> newComments){
        comments = newComments;
    }

    public void rateMovie(Map<String, Object> args){
        RateMovie prevRate = (RateMovie) rates.getById((String) args.get("userEmail"));

        if(prevRate == null){
            ratersCount++;
        }else {
            sumRating -= prevRate.score;
        }
        sumRating += (Integer) args.get("score");
        rates.add(new RateMovie(args));
    }

}
