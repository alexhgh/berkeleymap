import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */
    LinkedHashMap<Long, Vertex> db = new LinkedHashMap<>();
    Trie locationlist = new Trie();
    private int size;

    public GraphDB() {
        db = new LinkedHashMap<>();
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        ArrayList<Long> pending = new ArrayList<>();
        for (long id : db.keySet()) {
            if (db.get(id).adj.isEmpty()) {
                pending.add(id);
            }
        }
        for (long id : pending) {
            removeNode(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     */
    Iterable<Long> vertices() {
        ArrayList<Long> sol = new ArrayList<>();
        boolean seen = false;
        long target = Long.parseLong("4005231815");
        for (long id : db.keySet()) {
            sol.add(id);
            if (id == target) {
                seen = true;
            }
        }
        return sol;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     */
    Iterable<Long> adjacent(long v) {
        return db.get(v).adj;
    }

    /**
     * Returns the Euclidean distance between vertices v and w, where Euclidean distance
     * is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ).
     */
    double distance(long v, long w) {
        double londiff = (db.get(v).lon - db.get(w).lon) * (db.get(v).lon - db.get(w).lon);
        double latdiff = (db.get(v).lat - db.get(w).lat) * (db.get(v).lat - db.get(w).lat);
        return Math.sqrt(londiff + latdiff);
    }

    /**
     * Returns the vertex id closest to the given longitude and latitude.
     */
    long closest(double lon, double lat) {
        long closestID = 0;
        double mindis = -1.0;
        for (long id : db.keySet()) {
            if (mindis == -1.0) {
                mindis = Math.sqrt(((db.get(id).lon - lon) * (db.get(id).lon - lon))
                        + ((db.get(id).lat - lat) * (db.get(id).lat - lat)));
            } else {
                double dis = Math.sqrt(((db.get(id).lon - lon) * (db.get(id).lon - lon))
                        + ((db.get(id).lat - lat) * (db.get(id).lat - lat)));
                if (mindis > dis) {
                    mindis = dis;
                    closestID = id;
                }
            }
        }
        return closestID;
    }

    /**
     * Longitude of vertex v.
     */
    double lon(long v) {
        if (db.containsKey(v)) {
            return db.get(v).lon;
        } else {
            return (long) -1;
        }
    }

    /**
     * Latitude of vertex v.
     */
    double lat(long v) {
        if (db.containsKey(v)) {
            return db.get(v).lat;
        } else {
            return (long) -1;
        }
    }

    /**
     * add Node.
     */
    void addNode(long id, double lon, double lat) {
        size = size + 1;
        if (!db.containsKey(id)) {
            Vertex temp = new Vertex(lon, lat);
            db.put(id, temp);
        }
    }

    void addEdge(long tag, long srcID, long dstID) {
        if (db.containsKey(srcID) && db.containsKey(dstID)) {
            Edge tempsrc = new Edge(tag, srcID, dstID);
            Edge tempdst = new Edge(tag, dstID, srcID);
            db.get(srcID).adj.add(dstID);
            db.get(dstID).adj.add(srcID);
            db.get(srcID).connection.add(tempsrc);
            db.get(dstID).connection.add(tempdst);
        }
    }

    void addWay(ArrayList<Long> node, long tag) {
        for (int i = 0; i < node.size() - 1; i++) {
            long srcID = node.get(i);
            long dstID = node.get(i + 1);
            addEdge(tag, srcID, dstID);
        }
    }

    void removeNode(long id) {
        if (db.containsKey(id)) {
            db.remove(id);
            size = size - 1;
        }
    }

    private int size() {
        return size;
    }

    public static void main(String[] args) {
        GraphDB g = new GraphDB();
        g.addNode(1, 1.1, 1.1);
        g.addNode(2, 2.2, 2.2);
        g.addNode(3, 3.3, 3.3);
        g.addNode(4, 4.4, 4.4);
        g.addEdge(12, 1, 2);
        g.addEdge(23, 2, 3);
        g.addEdge(13, 1, 3);
        System.out.println(g.size());
        ArrayList<Long> temp;
        temp = (ArrayList<Long>) g.vertices();
        System.out.println(temp.get(0));
        System.out.println("x: " + g.lon(1) + " y: " + g.lat(1));
        g.clean();
        System.out.println(g.size());
    }
}
