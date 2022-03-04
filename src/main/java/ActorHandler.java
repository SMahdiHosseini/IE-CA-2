import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActorHandler implements Handler{
    public IEMDB iemdb;

    public ActorHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }

    private String makeHtml(Map<String, Object> actorJson){
        ArrayList<Map<String, Object>> actedMovies = (ArrayList<Map<String, Object>>) actorJson.get("Movies");
        String result =
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Actor</title>\n" +
                "    <style>\n" +
                "        li {\n" +
                "        \tpadding: 5px\n" +
                "        }\n" +
                "        table{\n" +
                "            width: 40%;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <ul>\n" +
                "        <li id=\"name\">name: " + actorJson.get("name") + "</li>\n" +
                "        <li id=\"birthDate\">birthDate: " + actorJson.get("birthDate") + "</li>\n" +
                "        <li id=\"nationality\">nationality: " + actorJson.get("nationality") + "</li>\n" +
                "        <li id=\"tma\">Total movies acted in: " + actedMovies.size() + "</li>\n" +
                "    </ul>\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <th>Movie</th>\n" +
                "            <th>imdb Rate</th> \n" +
                "            <th>rating</th> \n" +
                "            <th>page</th> \n" +
                "        </tr>\n";
        for (int i = 0; i < actedMovies.size(); i++) {
            result += "        <tr>\n" +
                    "            <td> " + actedMovies.get(i).get("name") + "</td>\n" +
                    "            <td> " + actedMovies.get(i).get("imdb Rate") + "</td> \n" +
                    "            <td> " + actedMovies.get(i).get("rating") + "</td> \n" +
                    "            <td><a href=\"/movies/" + actedMovies.get(i).get("movieId") + "\">Link</a></td>\n" +
                    "        </tr>\n";
        }
        result +=
                "\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";
        return result;
    }

    @Override
    public void handle(Context ctx) {
        String actor_id = ctx.pathParam("actor_id");
        Map<String, Object> actorJson = iemdb.getActorJson(actor_id);
        if (actorJson == null){
            ctx.redirect("/404/ActorNotFound");
            return;
        }

        ctx.html(makeHtml(actorJson));
    }
}
