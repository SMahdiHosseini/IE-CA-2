import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.Map;

class MoviesHandler implements Handler {
    public IEMDB iemdb;
    public MoviesHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }
    @Override
    public void handle(Context ctx) {
        ArrayList<Map<String, Object>> moviesList = iemdb.getMoviesListJson();
        String resultString = "";
        for (Map<String, Object> stringObjectMap : moviesList) {
            resultString = resultString.concat((String) stringObjectMap.get("name")).concat("\n");
        }

        ctx.result(resultString);
    }
}