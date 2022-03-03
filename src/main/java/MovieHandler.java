import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.Map;

class MovieHandler implements Handler {
    public IEMDB iemdb;
    public MovieHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }
    @Override
    public void handle(Context ctx) {
        String pp = ctx.pathParam("movie_id");
        ctx.result(pp);
    }
}