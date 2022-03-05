import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.Map;

class rateHandler implements Handler {
    public IEMDB iemdb;

    public rateHandler(IEMDB _iemdb) {
        iemdb = _iemdb;
    }

    @Override
    public void handle(Context ctx) {

        String movie_id = ctx.pathParam("movie_id");
        String user_id = ctx.pathParam("user_id");
        String rate = ctx.pathParam("rate");
        Map<String, Object> response = ServerUtils.extractJsonToMap(iemdb.rateMovie("{\"score\":" + rate + ", \"movieId\":" + movie_id + ", \"userEmail\": \"" + user_id + "\"}"));
        if((boolean)response.get("success")){
            ctx.redirect("/200/{done}");
        }else{
            ctx.redirect("/403/{wrong inputs}");
        }
    }
}