import io.javalin.Javalin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Server {
    public static Javalin app;
    private static IEMDB iemdb;

    public static void main(String[] args) throws IOException {
        app = Javalin.create().start(7070);
        iemdb = new IEMDB();
        app.get("/", ctx -> ctx.result("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t Welcome to IEMDB"));
        iemdb.prepareIEMDB();
    }
}