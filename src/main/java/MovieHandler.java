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
        Movie movie = iemdb.getMovie(pp);
        String resultString =
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>Movie</title>\n" +
                "    <style>\n" +
                "      li {\n" +
                "        padding: 5px;\n" +
                "      }\n" +
                "      table {\n" +
                "        width: 20%;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    \n" +
                "    <ul>\n" +
                "      <li id=\"name\">name: " + movie.name + "</li>\n" +
                "      <li id=\"summary\">" + movie.summary + "</li>\n" +
                "      <li id=\"releaseDate\">releaseDate: " + movie.releaseDateString + "</li>\n" +
                "      <li id=\"director\">director: " + movie.director + "</li>\n" +
                "      <li id=\"writers\">writers: " + String.join(", ", movie.writers) + "</li>\n" +
                "      <li id=\"genres\">genres: " + String.join(", ", movie.genres) + "</li>\n" +
                "      <li id=\"cast\">cast: Tom Hanks, Gray sinise</li>\n" +
                "      <li id=\"imdbRate\">imdb Rate: " + movie.imdbRate + "</li>\n" +
                "      <li id=\"rating\">rating: " + movie.getRating() + "</li>\n" +
                "      <li id=\"duration\">duration: " + movie.duration + " minutes</li>\n" +
                "      <li id=\"ageLimit\">ageLimit: " + movie.ageLimit + "</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <label>Your ID:</label>\n" +
                "    <input type=\"text\" name=\"user_id\" value=\"\" />\n" +
                "    <br><br>\n" +
                "    <form action=\"\" method=\"POST\">\n" +
                "      <label>Rate(between 1 and 10):</label>\n" +
                "      <input type=\"number\" id=\"quantity\" name=\"quantity\" min=\"1\" max=\"10\">\n" +
                "      <button type=\"submit\">rate</button>\n" +
                "    </form>\n" +
                "    <br>\n" +
                "    <form action=\"\" method=\"POST\">\n" +
                "      <button type=\"submit\">Add to WatchList</button>\n" +
                "    </form>\n" +
                "    <br />\n" +
                "    <table>\n" +
                "      <tr>\n" +
                "        <th>nickname</th>\n" +
                "        <th>comment</th>\n" +
                "        <th></th>\n" +
                "        <th></th>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>@sara</td>\n" +
                "        <td>Nice</td>\n" +
                "        <td>\n" +
                "          <form action=\"\" method=\"POST\">\n" +
                "            <label for=\"\">3</label>\n" +
                "            <input\n" +
                "              id=\"form_comment_id\"\n" +
                "              type=\"hidden\"\n" +
                "              name=\"comment_id\"\n" +
                "              value=\"03\"\n" +
                "            />\n" +
                "            <button type=\"submit\">like</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "          <form action=\"\" method=\"POST\">\n" +
                "            <label for=\"\">1</label>\n" +
                "            <input\n" +
                "              id=\"form_comment_id\"\n" +
                "              type=\"hidden\"\n" +
                "              name=\"comment_id\"\n" +
                "              value=\"03\"\n" +
                "            />\n" +
                "            <button type=\"submit\">dislike</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>@mehdi</td>\n" +
                "        <td>Good</td>\n" +
                "        <td>\n" +
                "          <form action=\"\" method=\"POST\">\n" +
                "            <label for=\"\">2</label>\n" +
                "            <input\n" +
                "              id=\"form_comment_id\"\n" +
                "              type=\"hidden\"\n" +
                "              name=\"comment_id\"\n" +
                "              value=\"02\"\n" +
                "            />\n" +
                "            <button type=\"submit\">like</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "          <form action=\"\" method=\"POST\">\n" +
                "            <label for=\"\">2</label>\n" +
                "            <input\n" +
                "              id=\"form_comment_id\"\n" +
                "              type=\"hidden\"\n" +
                "              name=\"comment_id\"\n" +
                "              value=\"02\"\n" +
                "            />\n" +
                "            <button type=\"submit\">dislike</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>@jack</td>\n" +
                "        <td>amazing</td>\n" +
                "        <td>\n" +
                "          <form action=\"\" method=\"POST\">\n" +
                "            <label for=\"\">3</label>\n" +
                "            <input\n" +
                "              id=\"form_comment_id\"\n" +
                "              type=\"hidden\"\n" +
                "              name=\"comment_id\"\n" +
                "              value=\"01\"\n" +
                "            />\n" +
                "            <button type=\"submit\">like</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "          <form action=\"\" method=\"POST\">\n" +
                "            <label for=\"\">5</label>\n" +
                "            <input\n" +
                "              id=\"form_comment_id\"\n" +
                "              type=\"hidden\"\n" +
                "              name=\"comment_id\"\n" +
                "              value=\"01\"\n" +
                "            />\n" +
                "            <button type=\"submit\">dislike</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n";
        ctx.html(resultString);
    }
}