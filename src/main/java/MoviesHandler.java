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
//            resultString = resultString.concat((String) movie.get("name")).concat("\n");
            System.out.println(String.join(" ,", (ArrayList<String>) movie.get("cast")));
            resultString +=
                            "        <tr>\n" +
                            "            <td>" + movie.get("name") + "</td>\n" +
                            "            <td>" + movie.get("summary") + "</td> \n" +
                            "            <td>" + movie.get("releaseDate") + "</td>\n" +
                            "            <td>" + movie.get("director") + "</td>\n" +
                            "            <td>" + String.join(" ,", (ArrayList<String>) movie.get("cast")) + "</td>\n" +
                            "            <td>Drama, Romance</td>\n" +
                            "            <td>Tom Hanks, Gray sinise</td>\n" +
                            "            <td>8.8</td>\n" +
                            "            <td>8</td>\n" +
                            "            <td>142</td>\n" +
                            "            <td>9</td>\n" +
                            "            <td><a href=\"/movies/01\">Link</a></td>\n" +
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