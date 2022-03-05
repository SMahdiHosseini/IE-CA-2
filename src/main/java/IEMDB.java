import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.util.resource.JarResource;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class IEMDB {
    private Repository users;
    private Repository actors;
    private Repository movies;
    private Repository comments;

    private Integer lastCommentId;

    public IEMDB(){
        users = new Repository();
        actors = new Repository();
        movies = new Repository();
        comments = new Repository();
        lastCommentId = 0;
    }

    private Map<String, Object> extractJsonToMap(String args){
        Map<String, Object> mapping = new HashMap<>();

        try {
            mapping = new ObjectMapper().readValue(args, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

//        for (String key: mapping.keySet()){
//            System.out.println(key+ " = " + mapping.get(key));
//        }
        return mapping;
    }
    private String makeResponse(boolean success, Object data){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("data", data);
        responseMap.put("success", success);

        try {
            return objectMapper.writeValueAsString(responseMap);
        } catch (IOException e) {
            return "";
        }
    }

    private boolean isActorExists(String id){
        Entity e = actors.getById(id);
        return e != null;
    }

    private boolean isActorsExists(ArrayList<Integer> cast){
        for (Integer actor_id : cast)
            if (!isActorExists(Integer.toString(actor_id)))
                return false;

        return true;
    }

    private ArrayList<Object> getActorsJsons(ArrayList<Integer> cast){
        ArrayList<Object> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Integer actor_id : cast)
            result.add(actors.getById(Integer.toString(actor_id)).getJsonMap());
        return result;
    }

    public ArrayList<Object> getCommentsJsons(String movieId){
        ArrayList<Object> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < comments.size(); i++) {
            Comment comment = (Comment) comments.getByIndex(i);
            if(!comment.movieId.equals(movieId))
                continue;
            result.add(comment);
        }
        return result;
    }

    private String addActor(String args){
        actors.add(new Actor(extractJsonToMap(args)));
        return makeResponse(true, "actor added successfully");
    }

    private String addMovie(String args){
        Map<String, Object> mapping = extractJsonToMap(args);

        if (!isActorsExists((ArrayList<Integer>) mapping.get("cast")))
            return makeResponse(false, "ActorNotFound");

        ArrayList<Object> actorsJson = getActorsJsons((ArrayList<Integer>) mapping.get("cast"));
//        System.out.println(getActorsJsons((ArrayList<Integer>) mapping.get("cast")));

        movies.add(new Movie(mapping, actorsJson));
        return makeResponse(true, "Movie added successfully");
    }

    private String addUser(String args){
        users.add(new Client(extractJsonToMap(args)));
        return makeResponse(true, "user added successfully");
    }

    private String addComment(String args){
        Map<String, Object> mapping = extractJsonToMap(args);

        if (users.getById((String) mapping.get("userEmail")) == null)
            return makeResponse(false, "UserNotFound");

        if ((Movie) movies.getById(String.valueOf(mapping.get("movieId"))) == null)
            return makeResponse(false, "MovieNotFound");

        Integer newId = ++lastCommentId;
        comments.add(new Comment(mapping, newId));
        return makeResponse(true, "comment with id " + String.valueOf(newId) + " added successfully");
    }

    public String rateMovie(String args){
        Map<String, Object> mapping = extractJsonToMap(args);

        if ((Integer) mapping.get("score") < 1 || (Integer) mapping.get("score") > 10)
            return makeResponse(false, "InvalidRateScore");

        if (users.getById((String) mapping.get("userEmail")) == null)
            return makeResponse(false, "UserNotFound");

        Movie movie = (Movie) movies.getById(String.valueOf(mapping.get("movieId")));
        if (movie == null)
            return makeResponse(false, "MovieNotFound");

        movie.rateMovie(mapping);
        return makeResponse(true, "movie rated successfully");
    }

    public String voteComment(Map<String, Object> mapping){
//        Map<String, Object> mapping = extractJsonToMap(args);

        if ((Integer) mapping.get("vote") != 1 && (Integer) mapping.get("vote") != 0 && (Integer) mapping.get("vote") != -1)
            return makeResponse(false, "InvalidVoteValue");

        if (users.getById((String) mapping.get("userEmail")) == null)
            return makeResponse(false, "UserNotFound");

        Comment comment = (Comment) comments.getById(String.valueOf(mapping.get("commentId")));
        if (comment == null)
            return makeResponse(false, "CommentNotFound");

        comment.voteComment(mapping);
        return makeResponse(true, "comment voted successfully");
    }

    public ArrayList<Map<String, Object>> getMoviesByDate(String start, String end){
        Date startingDate = null;
        Date endingDate = null;
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        DateFormat formatter = new SimpleDateFormat("yyyy");
        try {
            startingDate = formatter.parse(start);
            endingDate = formatter.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < movies.size(); i++){
            Movie movie = (Movie) movies.getByIndex(i);
            if(movie.releaseDate.after(startingDate) && movie.releaseDate.before(endingDate)){
                result.add(movie.getJsonMap());
            }
        }
        return result;
    }

    public String addToWatchList(Map<String, Object> mapping){
//        Map<String, Object> mapping = extractJsonToMap(args);

        Client user = (Client) users.getById((String) mapping.get("userEmail"));
        if (user == null)
            return makeResponse(false, "UserNotFound");

        Movie movie = (Movie) movies.getById(String.valueOf(mapping.get("movieId")));
        if (movie == null)
            return makeResponse(false, "MovieNotFound");

        if (user.legalToWatch(movie.ageLimit))
            if (user.addToWatchList(mapping))
                return makeResponse(true, "movie added to watchlist successfully");
            else
                return makeResponse(false, "MovieAlreadyExists");
        else
                return makeResponse(false, "AgeLimitError");
    }

    public String removeFromWatchList(Map<String, Object> mapping){
        Client user = (Client) users.getById((String) mapping.get("userEmail"));
        if (user == null)
            return makeResponse(false, "UserNotFound");

        Movie movie = (Movie) movies.getById(String.valueOf(mapping.get("movieId")));
        if (movie == null)
            return makeResponse(false, "MovieNotFound");

        if (user.removeFromWatchList(mapping))
            return makeResponse(true, "movie removed from watchlist successfully");
        else
            return makeResponse(false, "MovieNotFound");
    }

    private String getMoviesList(){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, ArrayList<Map<String, Object>>> moviesJsons = new HashMap<>();
        moviesJsons.put("MoviesList", movies.getEntitiesJsons());
        return makeResponse(true, moviesJsons);
    }

    public ArrayList<Map<String, Object>> getMoviesListJson(){
        return movies.getEntitiesJsons();
    }

    public Movie getMovie(String movie_ID){
        return (Movie) movies.getById(movie_ID);
    }

    private String getMovieById(String args){
        Map<String, Object> mapping = extractJsonToMap(args);

        Movie movie = (Movie) movies.getById(String.valueOf(mapping.get("movieId")));
        if (movie == null)
            return makeResponse(false, "MovieNotFound");

        movie.updateComment(getCommentsJsons(movie.id));
        return makeResponse(true, movie.getJsonMap_Id());
    }

    private boolean checkValidGenre(Movie movie, String genre){
        for (String movieGenre : movie.genres)
            if (movieGenre.equals(genre))
                return true;
        return false;
    }

    private ArrayList<Movie> findByGenre(String genre){
        ArrayList<Movie> result = new ArrayList<>();
        Movie movie;
        for (int i = 0; i < movies.size(); i++){
            movie = (Movie) movies.getByIndex(i);
            if (checkValidGenre(movie, genre))
                result.add(movie);
        }
        return result;
    }

    public ArrayList<Map<String, Object>> getMoviesByGenre(String genre){
        ArrayList<Map<String, Object>> moviesJsons = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        for (Movie movie : findByGenre(genre))
            moviesJsons.add(movie.getJsonMap());

        return moviesJsons;
    }

    private ArrayList<Map<String, Object>> getWatchlistJson(Repository watchlist){
        ArrayList<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < watchlist.size(); i++)
            result.add(((Movie) movies.getById(((WatchListItem) watchlist.getByIndex(i)).id)).getJsonMap_genre());

        return result;
    }

    public String getWatchList(String userEmail){
        Map<String, Object> result = new HashMap<>();

        Client user = (Client) users.getById(userEmail);
        if (user == null)
            return makeResponse(false, "UserNotFound");

        result.put("WatchList", getWatchlistJson(user.watchList));
        return makeResponse(true, result);
    }

    public Object getActedMovies(String actor_id){
        ArrayList<Object> actedMoviesJson = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++){
            Movie movie = (Movie) movies.getByIndex(i);
            if (movie.actedInThisMovie(Integer.valueOf(actor_id)))
                actedMoviesJson.add(movie.getJsonForActor());
        }
        return actedMoviesJson;
    }

    public Map<String, Object> getActorJson(String actor_id){
        Actor actor = (Actor) actors.getById(actor_id);
        Map<String, Object> actorJson = new HashMap<>();

        if (actor == null)
            return null;

        actorJson = actor.getJsonMap();
        actorJson.put("Movies", getActedMovies(actor_id));
        return actorJson;
    }

    public String handleCommand(String command){
        String[] splitedCommand = command.split(" ", 2);

        if (splitedCommand[0].equals("exit"))
            return "";

        if (splitedCommand[0].equals("addActor"))
            return this.addActor(splitedCommand[1]);

        if (splitedCommand[0].equals("addMovie"))
            return this.addMovie(splitedCommand[1]);

        if (splitedCommand[0].equals("addUser"))
            return this.addUser(splitedCommand[1]);

        if (splitedCommand[0].equals("addComment"))
            return this.addComment(splitedCommand[1]);

        if (splitedCommand[0].equals("rateMovie"))
            return this.rateMovie(splitedCommand[1]);

        if (splitedCommand[0].equals("voteComment"))
            return this.voteComment(ServerUtils.extractJsonToMap(splitedCommand[1]));

        if (splitedCommand[0].equals("addToWatchList"))
            return this.addToWatchList(ServerUtils.extractJsonToMap(splitedCommand[1]));

//        if (splitedCommand[0].equals("removeFromWatchList"))
//            return this.removeFromWatchList(splitedCommand[1]);

        if (splitedCommand[0].equals("getMoviesList"))
            return this.getMoviesList();

        if (splitedCommand[0].equals("getMovieById"))
            return this.getMovieById(splitedCommand[1]);

        if (splitedCommand[0].equals("getMoviesByGenre"))
            return makeResponse(true, this.getMoviesByGenre((String) ServerUtils.extractJsonToMap(splitedCommand[1]).get("genre")));

        if (splitedCommand[0].equals("getWatchList"))
            return this.getWatchList((String) ServerUtils.extractJsonToMap(splitedCommand[1]).get("userEmail"));

        return makeResponse(false, "InvalidCommand");
    }

    public void run(String url){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String response = "";
            try {
                response = handleCommand(scanner.nextLine());
                if (response.isEmpty())
                    break;
                System.out.println(response);
            }
            catch (Exception e){
                System.out.println(makeResponse(false, "InvalidCommand"));
            }
        }
    }
    private ArrayList<Object> getItems(String args){
        ArrayList<Object> result = new ArrayList<>();

        try {
            result = new ObjectMapper().readValue(args, ArrayList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getResponse(String apiURL) throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private void cloneItem(Method method, String url) throws IOException {
        String response = getResponse(url);
        ArrayList<Object> items = getItems(response);
        ObjectMapper objectMapper = new ObjectMapper();

        for (Object item : items){
            try {
                System.out.println(method.invoke(this, objectMapper.writeValueAsString(item)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareIEMDB() throws IOException {
        try {
            cloneItem(IEMDB.class.getDeclaredMethod("addActor", String.class), "http://138.197.181.131:5000/api/actors");
            cloneItem(IEMDB.class.getDeclaredMethod("addUser", String.class), "http://138.197.181.131:5000/api/users");
            cloneItem(IEMDB.class.getDeclaredMethod("addMovie", String.class), "http://138.197.181.131:5000/api/movies");
            cloneItem(IEMDB.class.getDeclaredMethod("addComment", String.class), "http://138.197.181.131:5000/api/comments");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
