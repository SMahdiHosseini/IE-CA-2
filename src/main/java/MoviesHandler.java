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
        ArrayList<Map<String, Object>> moviesList = new ArrayList<>();
        if (ctx.path().contains("search"))
            if (ctx.pathParam("genre") != null)
                moviesList = iemdb.getMoviesByGenre(ctx.pathParam("genre"));
//            else

        else
            moviesList = iemdb.getMoviesListJson();

        String resultString = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Movies</title>\n" +
                "    <style>\n" +
                "        table{\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <th>name</th>\n" +
                "            <th>summary</th> \n" +
                "            <th>releaseDate</th>\n" +
                "            <th>director</th>\n" +
                "            <th>writers</th>\n" +
                "            <th>genres</th>\n" +
                "            <th>cast</th>\n" +
                "            <th>imdb Rate</th>\n" +
                "            <th>rating</th>\n" +
                "            <th>duration</th>\n" +
                "            <th>ageLimit</th>\n" +
                "            <th>Links</th>\n" +
                "        </tr>\n";
        for (Map<String, Object> movie : moviesList) {
            resultString +=
                            "        <tr>\n" +
                            "            <td>" + movie.get("name") + "</td>\n" +
                            "            <td>" + movie.get("summary") + "</td> \n" +
                            "            <td>" + movie.get("releaseDate") + "</td>\n" +
                            "            <td>" + movie.get("director") + "</td>\n" +
                            "            <td>" + String.join(",", (ArrayList<String>) movie.get("writers")) + "</td>\n" +
                            "            <td>" + String.join(",", (ArrayList<String>) movie.get("genres")) + "</td>\n" +
                            "            <td>" + String.join(",", ServerUtils.makeListActorName((ArrayList<Object>) movie.get("cast"))) + "</td>\n" +
                            "            <td>" + movie.get("imdb Rate") + "</td>\n";
                            if(movie.get("rating") != null)
            resultString +=
                            "            <td>" + (double) movie.get("rating") + "</td>\n" ;
                            else
            resultString +=
                            "            <td>null</td>\n" ;
            resultString +=
                            "            <td>" + movie.get("duration") + "</td>\n" +
                            "            <td>" + movie.get("ageLimit") + "</td>\n" +
                            "            <td><a href=\"/movies/" + Integer.toString((int)movie.get("movieId")) + "\">Link</a></td>\n" +
                            "        </tr>\n";
        }

        resultString +=
                "    </table>\n" +
                "</body>\n" +
                "</html>";


        ctx.html(resultString);
//        ctx.result(resultString);
    }
}