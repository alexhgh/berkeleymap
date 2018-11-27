import java.util.HashSet;
import java.util.LinkedHashSet;

public class Vertex {
    public double lon;
    public double lat;
    public LinkedHashSet<Long> adj;
    public HashSet<Edge> connection;
    public String name = "";

    Vertex(double x, double y) {
        lon = x;
        lat = y;
        adj = new LinkedHashSet<>();
        connection = new HashSet<>();
    }

    Vertex(String name, double x, double y) {
        lon = x;
        lat = y;
        this.name = name;
        adj = new LinkedHashSet<>();
        connection = new HashSet<>();
    }
}