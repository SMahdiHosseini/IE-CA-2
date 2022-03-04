import io.javalin.http.Context;
import io.javalin.http.Handler;

public class AddToWatchlistHandler implements Handler {
    public IEMDB iemdb;

    public AddToWatchlistHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }

    @Override
    public void handle(Context ctx) {
        String user_id = ctx.pathParam("user_id");
        String movie_id = ctx.pathParam("movie_id");
//        iemdb
    }
}
