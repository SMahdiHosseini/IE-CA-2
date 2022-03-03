import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comment extends Entity{
    Repository votes;
    String userId;
    String movieId;
    String text;
    Date date;
    int like;
    int dislike;

    public Comment(int _id, String _userId, String _movieId, String _text){
        id = String.valueOf(_id);
        userId = _userId;
        movieId = _movieId;
        text = _text;
        like = 0;
        dislike = 0;
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        date = new Date(System.currentTimeMillis());
        votes = new Repository();
    }

    public Comment(Map<String, Object> args, int _id){
        this(_id, (String) args.get("userEmail"), String.valueOf(args.get("movieId")), (String) args.get("text"));
    }

    public void voteComment(Map<String, Object> args){
        VoteComment prevVote = (VoteComment) votes.getById((String) args.get("userEmail"));

        if(prevVote != null){
            if(prevVote.vote == 1)
                like--;
            else if (prevVote.vote == -1)
                dislike--;
        }
        if((int) args.get("vote") == 1)
            like++;
        else if((int) args.get("vote") == -1)
            dislike++;
        votes.add(new VoteComment(args));
    }

    public Map<String, Object> getJsonMap(){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("commentId", Integer.parseInt(id));
        jsonMap.put("userEmail", userId);
        jsonMap.put("text", text);
        jsonMap.put("like", like);
        jsonMap.put("dislike", dislike);
        return jsonMap;
    }
}
