import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerUtils {
    public void ServerUtil() { };

    public static Map<String, Object> extractJsonToMap(String args){
        Map<String, Object> mapping = new HashMap<>();

        try {
            mapping = new ObjectMapper().readValue(args, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public static ArrayList<String> makeListActorName(ArrayList<Object> castJson){
        ArrayList<String> actorList = new ArrayList<>();
        for (Object o : castJson) {
            actorList.add((String) ((Map<String, Object>) o).get("name"));
        }
        return actorList;
    }
}
