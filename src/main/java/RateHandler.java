import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.Map;

class RateHandler implements Handler {
    public IEMDB iemdb;

    public RateHandler(IEMDB _iemdb) {
        iemdb = _iemdb;
    }

    @Override
    public void handle(Context ctx) {
        String movie_id = "";
        String user_id = "";
        String rate = "";
        if (ctx.method().equals("POST")){
            movie_id = ctx.formParam("movie_id");
            user_id = ctx.formParam("user_id");
            rate = ctx.formParam("rate");
        }else if (ctx.method().equals("GET")){
            movie_id = ctx.pathParam("movie_id");
            user_id = ctx.pathParam("user_id");
            rate = ctx.pathParam("rate");
        } // error handle null return
        Map<String, Object> response = ServerUtils.extractJsonToMap(iemdb.rateMovie("{\"score\":" + rate + ", \"movieId\":" + movie_id + ", \"userEmail\": \"" + user_id + "\"}"));
        if((boolean)response.get("success")){
            if (ctx.method().equals("GET")) {
                ctx.redirect("/200/{done}");
            }else{
                ctx.redirect("/movies/" + movie_id);
            }
        }else{
            ctx.redirect("/403/{"+ response.get("data") +"}");
        }
    }
}