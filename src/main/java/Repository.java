import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class Repository {
    ArrayList<Entity> list;
    ArrayList<String> listId;
    public Repository(){
        list = new ArrayList<Entity>();
        listId = new ArrayList<String>();
    }

    public void add(@NotNull Entity e){
        String id = e.id;
        int index = listId.indexOf(id);
        if (index >= 0){
            list.set(index, e);
            return;
        }
        list.add(e);
        listId.add(id);
    }

    public boolean remove(String id){
        if(getById(id) == null){
            return false;
        }
        int indexToRemove = listId.indexOf(id);
        list.remove(indexToRemove);
        listId.remove(indexToRemove);
        return true;
    }

    public Entity getByIndex(int index){
        return list.get(index);
    }

    public Entity getById(String id){
        if (listId.contains(id))
            return list.get(listId.indexOf(id));
        return null;
    }

    public int size(){
        return list.size();
    }

    public int indexOf(String id){
        return listId.indexOf(id);
    }

    public ArrayList<Map<String, Object>> getEntitiesJsons(){
        ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Entity e : list)
            result.add(e.getJsonMap());
        return result;
    }
}
