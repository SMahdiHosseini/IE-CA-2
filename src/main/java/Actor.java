import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Actor extends Entity {
    String name;
    Date birthDate;
    String nationality;
    String birthDate_string;
    public Actor(int _id, String _name, String _birthDate, String _nationality){
        id = String.valueOf(_id);
        name = _name;
        nationality = _nationality;
        birthDate_string = _birthDate;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthDate = formatter.parse(_birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Actor(Map<String, Object> args){
        this((Integer) args.get("id"), (String) args.get("name"), (String) args.get("birthDate"), (String) args.get("nationality"));
    }

    public Map<String, Object> getJsonMap(){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("actorId", Integer.parseInt(id));
        jsonMap.put("name", name);
        jsonMap.put("birthDate", birthDate_string);
        jsonMap.put("nationality", nationality);
        return jsonMap;
    }
}
