import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotFound implements Handler{
    private String makeHtml(String message){
        String result = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>404 Error</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>404<br> " + message + "</h1>\n" +
                "</body>\n" +
                "</html>";
        return result;
    }
    @Override
    public void handle(Context ctx){

        ctx.html(makeHtml(ctx.pathParam("message")));
    }
}
