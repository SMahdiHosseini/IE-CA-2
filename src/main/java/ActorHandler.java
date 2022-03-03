import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.Map;

public class ActorHandler implements Handler{
    public IEMDB iemdb;
    public ActorHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }
    private String makeHTML(Map<String, Object> actorJson){
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
                "        <li id=\"tma\">Total movies acted in: 3</li>\n" +
                "    </ul>\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <th>Movie</th>\n" +
                "            <th>imdb Rate</th> \n" +
                "            <th>rating</th> \n" +
                "            <th>page</th> \n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>No Time to Die</td>\n" +
                "            <td>7.3</td> \n" +
                "            <td>8</td> \n" +
                "            <td><a href=\"/movies/01\">Link</a></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>The Night Clerk</td>\n" +
                "            <td>5.6</td> \n" +
                "            <td>5</td> \n" +
                "            <td><a href=\"/movies/02\">Link</a></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>Knives Out</td>\n" +
                "            <td>7.9</td> \n" +
                "            <td>8</td> \n" +
                "            <td><a href=\"/movies/03\">Link</a></td>\n" +
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
        ctx.html(makeHTML(iemdb.getActorJson(actor_id)));
//        ctx.result(pp);
    }
}
