import java.util.Map;

abstract class Entity {
    public String id;
    abstract Map<String, Object> getJsonMap();

}
