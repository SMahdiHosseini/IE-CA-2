import io.javalin.Javalin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Server {
    public static Javalin app;
    public static IEMDB iemdb;

    public static void main(String[] args) throws IOException {
        app = Javalin.create().start(5050);
        iemdb = new IEMDB();
        app.get("/", ctx -> ctx.result("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t Welcome to IEMDB"));
        app.get("/movies", new MoviesHandler(iemdb));
        app.get("/movies/{movie_id}", new MovieHandler(iemdb));
        app.get("/watchList/{user_id}/{movie_id}", new WatchListHandler(iemdb));
        app.get("/actors/{actor_id}", new ActorHandler(iemdb));
        app.get("/404", new NotFound());
        iemdb.prepareIEMDB();
    }
}