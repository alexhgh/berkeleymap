import java.util.List;
import java.util.ArrayList;

/**
 * This class provides a main method for experimenting with GraphDB construction.
 * You could also use MapServer, but this class lets you play around with
 * GraphDB in isolation from all the rest of the parts of this assignment.
 */
public class GraphDBLauncher {
    private static final String OSM_DB_PATH = "berkeley.osm";

    public static void main(String[] args) {
        GraphDB g = new GraphDB(OSM_DB_PATH);

        System.out.println("adj");
        System.out.println("begin point: " + g.closest(-122.23354274523257, 37.87383979834944));
        System.out.println("dst point: " + g.closest(-122.23354274523257, 37.86020837234193));
        Iterable<Long> ad = g.adjacent(g.closest(-122.23307272570244, 37.87383979834944));

        int level = 1;
        for (long i : ad) {
            System.out.print("lv: " + level + ": " + i + ", ");
            System.out.println();
        }
        System.out.println(g.adjacent(53055000L));
        System.out.println(g.distance(g.closest(-122.23354274523257, 37.87383979834944), 56819228));
        System.out.println(g.distance(g.closest(-122.23354274523257, 37.87383979834944), 35719103));
    }
}
