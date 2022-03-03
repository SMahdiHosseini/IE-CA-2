import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.Map;

class WatchListHandler implements Handler {
    public IEMDB iemdb;
    public WatchListHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }
    @Override
    public void handle(Context ctx) {
        String movie_id = ctx.pathParam("movie_id");
        String user_id = ctx.pathParam("user_id");
        ctx.result(movie_id.concat("\n").concat(user_id));
    }
}