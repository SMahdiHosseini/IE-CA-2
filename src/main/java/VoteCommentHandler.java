import io.javalin.http.Context;
import io.javalin.http.Handler;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteCommentHandler implements Handler {
    IEMDB iemdb;

    public VoteCommentHandler(IEMDB _iemdb){
        iemdb = _iemdb;
    }

    @Override
    public void handle(Context ctx){
        String user_id = "";
        String comment_id = "";
        String vote = "";
        String movie_id = "";
        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        if (ctx.method() == "POST"){
            user_id = ctx.formParam("user_id");
            comment_id = ctx.formParam("comment_id");
            vote = ctx.formParam("vote");
            movie_id = ctx.formParam("movie_id");
        }
        else {
            user_id = ctx.pathParam("user_id");
            comment_id = ctx.pathParam("comment_id");
            vote = ctx.pathParam("vote");
        }

        mapping.put("vote", Integer.valueOf(vote));
        mapping.put("userEmail", user_id);
        mapping.put("commentId", Integer.valueOf(comment_id));

        result = ServerUtils.extractJsonToMap(iemdb.voteComment(mapping));

        if ((boolean) result.get("success") == false){
            if (result.get("data").equals("UserNotFound"))
                ctx.redirect("/404/UserNotFound");

            else if (result.get("data").equals("CommentNotFound"))
                ctx.redirect("/404/CommentNotFound");

            else
                ctx.redirect("/403/InvalidVoteValue");
            return;
        }

        if (ctx.method() == "POST")
            ctx.redirect("/movies/" + movie_id);
        else
            ctx.redirect("/200/" + result.get("data"));
    }
}
