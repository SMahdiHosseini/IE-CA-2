import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.Map;

class WatchListHandler implements Handler {
    public IEMDB iemdb;

    public WatchListHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }

    private String makeHtml(ArrayList<Map<String, Object>> wathcList){
        String result =
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>WatchList</title>\n" +
                        "    <style>\n" +
                        "        li {\n" +
                        "        \tpadding: 5px\n" +
                        "        }\n" +
                        "        table{\n" +
                        "            width: 100%;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Movie</th>\n" +
                        "            <th>releaseDate</th> \n" +
                        "            <th>director</th> \n" +
                        "            <th>genres</th> \n" +
                        "            <th>imdb Rate</th> \n" +
                        "            <th>rating</th> \n" +
                        "            <th>duration</th> \n" +
                        "            <th></th>\n" +
                        "            <th></th>\n" +
                        "        </tr>\n";
        for (int i = 0; i < wathcList.size(); i++) {
            Map<String, Object> watchListItem = wathcList.get(i);
            result +=
                        "        <tr>\n" +
                        "            <th> " + watchListItem.get("name") + "</th>\n" +
                        "            <th> " + watchListItem.get("releaseDate") + "</th> \n" +
                        "            <th> " + watchListItem.get("director") + "</th> \n" +
                        "            <th> " + watchListItem.get("genres") + "</th> \n" +
                        "            <th> " + watchListItem.get("imdb Rate") + "</th> \n" +
                        "            <th> " + watchListItem.get("rating") + "</th> \n" +
                        "            <th> " + watchListItem.get("duration") + "</th> \n" +
                        "            <td><a href=\"/movies/" + watchListItem.get("movieId") + "\">Link</a></td>\n" +
                        "            <td>        \n" +
                        "                <form action=\"\" method=\"POST\" >\n" +
                        "                    <input id=\"form_movie_id\" type=\"hidden\" name=\"movie_id\" value=\"" + watchListItem.get("movieId") + "\">\n" +
                        "                    <button type=\"submit\">Remove</button>\n" +
                        "                </form>\n" +
                        "            </td>\n" +
                        "        </tr>\n";
        }
        result +=
                        "    </table>\n" +
                        "</body>\n" +
                        "</html>";
        return result;
    }

    @Override
    public void handle(Context ctx) {
        String user_id = ctx.pathParam("user_id");
        Map<String, Object> result = ServerUtils.extractJsonToMap(iemdb.getWatchList(user_id));

        if ((boolean) result.get("success") == false){
            ctx.redirect("/404/" + result.get("data"));
            return;
        }

        ctx.html(makeHtml((ArrayList<Map<String, Object>>) ((Map<String, Object>)result.get("data")).get("WatchList")));
    }
}