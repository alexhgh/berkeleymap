import java.util.HashMap;
import java.util.Map;

public class Location {
    private Map<String, Object> data;


    public Location(String name, long id, double x, double y) {
        data = new HashMap<>();
        data.put("name", name);
        data.put("lon", x);
        data.put("id", id);
        data.put("lat", y);
    }

    public Map<String, Object> getMap() {
        return data;
    }
}
