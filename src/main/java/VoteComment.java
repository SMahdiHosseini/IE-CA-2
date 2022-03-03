import java.util.Map;

public class VoteComment extends Entity{
    int commentId;
    int vote;
    public VoteComment(String userEmail, int _commentId, int _vote){
        id = userEmail;
        commentId = _commentId;
        vote = _vote;
    }

    public VoteComment(Map<String, Object> args){
        this((String) args.get("userEmail"), (int) args.get("commentId"), (int) args.get("vote"));
    }

    public Map<String, Object> getJsonMap() {
        return null;
    }
}
