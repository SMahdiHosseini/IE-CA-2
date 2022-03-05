import io.javalin.http.Context;
import io.javalin.http.Handler;

import javax.swing.*;
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
        movie.updateComment(iemdb.getCommentsJsons(movie.id));
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
                "    <form action=\"/form/rateMovie\" method=\"POST\">\n" +
                        "    <label>Your ID:</label>\n" +
                        "    <input type=\"text\" name=\"user_id\" value=\"\" />\n" +
                "      <label>Rate(between 1 and 10):</label>\n" +
                "      <input type=\"number\" id=\"rate\" name=\"rate\" min=\"1\" max=\"10\">\n" +
                "      <input type=\"hidden\" id=\"movie_id\" name=\"movie_id\" value=\"" + movie.id + "\">\n" +
                "      <button type=\"submit\">rate</button>\n" +
                "    </form>\n" +
                "    <br>\n" +
                "    <form action=\"/form/addWatchList\" method=\"POST\">\n" +
                        "    <label>Your ID:</label>\n" +
                        "    <input type=\"text\" name=\"user_id\" value=\"\" />\n" +
                "      <button type=\"submit\">Add to WatchList</button>\n" +
                "    </form>\n" +
                "    <br />\n" +
                "    <table>\n" +
                "      <tr>\n" +
                "        <th>nickname</th>\n" +
                "        <th>comment</th>\n" +
                "        <th></th>\n" +
                "        <th></th>\n" +
                "      </tr>\n";
        System.out.println("comment count = " + movie.comments.size());
        for (int i = 0; i < movie.comments.size(); i++) {
            Comment comment = (Comment) movie.comments.get(i);
            System.out.println(comment.id);
            resultString +=
                    "      <tr>\n" +
                    "        <td>" + comment.userId + "</td>\n" +
                    "        <td>" + comment.text + "</td>\n" +
                    "        <td>\n" +
                    "          <form action=\"/form/vote\" method=\"POST\">\n" +
                            "    <label>Your ID:</label>\n" +
                            "    <input type=\"text\" name=\"user_id\" value=\"\" />\n" +
                            "</td><td>" +
                    "            <input\n" +
                    "              id=\"form_comment_id\"\n" +
                    "              type=\"hidden\"\n" +
                    "              name=\"comment_id\"\n" +
                    "              value=\"" + comment.id + "\"\n" +
                    "            />\n" +
                    "            <input\n" +
                    "              id=\"movie_id\"\n" +
                    "              type=\"hidden\"\n" +
                    "              name=\"movie_id\"\n" +
                    "              value=\"" + movie.id + "\"\n" +
                    "            />\n" +
                    "            <button name=\"vote\" type=\"submit\" value=\"1\">like</button>\n" +
                    "            <label for=\"\">" + String.valueOf(comment.like) + "</label>\n" +
                    "        </td>\n" +
                    "        <td>\n" +
                    "            <button name=\"vote\" type=\"submit\" value=\"-1\">dislike</button>\n" +
                    "            <label for=\"\">" + String.valueOf(comment.dislike) + "</label>\n" +
                    "          </form>\n" +
                    "        </td>\n" +
                    "      </tr>\n";
        }
        resultString +=
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n";
        ctx.html(resultString);
    }
}