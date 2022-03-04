import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.Map;

public class RemoveFromWatchList implements Handler {
    public IEMDB iemdb;

    public RemoveFromWatchList(IEMDB _iemdb){
        iemdb = _iemdb;
    }

    @Override
    public void handle(Context ctx){
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("userEmail", ctx.pathParam("user_id"));
        mapping.put("movieId", ctx.formParam("movie_id"));


        iemdb.removeFromWatchList(mapping);
        ctx.redirect("/watchList/" + ctx.pathParam("user_id"));
    }
}
