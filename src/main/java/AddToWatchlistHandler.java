import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.Map;

public class AddToWatchlistHandler implements Handler {
    public IEMDB iemdb;

    public AddToWatchlistHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }

    @Override
    public void handle(Context ctx) {
        String user_id = "";
        String movie_id = "";
        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        if (ctx.method() == "POST"){
            user_id = ctx.formParam("user_id");
            movie_id = ctx.formParam("movie_id");
        }
        else {
            user_id = ctx.pathParam("user_id");
            movie_id = ctx.pathParam("movie_id");
        }

        mapping.put("userEmail", user_id);
        mapping.put("movieId", Integer.valueOf(movie_id));

        result = ServerUtils.extractJsonToMap(iemdb.addToWatchList(mapping));

        if ((boolean) result.get("success") == false){
            if (result.get("data").equals("UserNotFound"))
                ctx.redirect("/404/UserNotFound");

            else if (result.get("data").equals("MovieNotFound"))
                ctx.redirect("/404/MovieNotFound");

            else if (result.get("data").equals("MovieAlreadyExists"))
                ctx.redirect("/403/MovieAlreadyExists");

            else
                ctx.redirect("/403/AgeLimitError");
            return;
        }

        if (ctx.method() == "POST")
            ctx.redirect("/movies/" + movie_id);
        else
            ctx.redirect("/200/" + result.get("data"));
    }
}
