import io.javalin.Javalin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Server {
    public static Javalin app;
    public static IEMDB iemdb;
    public static ServerUtils util;

    public static void main(String[] args) throws IOException {
        app = Javalin.create().start(5050);
        iemdb = new IEMDB();
        util = new ServerUtils();

        app.get("/", ctx -> ctx.result("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t Welcome to IEMDB"));
        app.get("/movies", new MoviesHandler(iemdb));
        app.get("/movies/{movie_id}", new MovieHandler(iemdb));
        app.get("/watchList/{user_id}", new WatchListHandler(iemdb));
        app.get("/actors/{actor_id}", new ActorHandler(iemdb));
        app.get("/watchList/{user_id}/{movie_id}", new AddToWatchlistHandler(iemdb));
        app.get("/404/{message}", new NotFound());
        app.get("/403/{message}", new Forbidden());
        app.get("/200/{message}", new Success());
        iemdb.prepareIEMDB();
    }
}