import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest,
     * where the longs are node IDs.
     */
    private static LinkedList<Long> solution;

    private static class SearchNode implements Comparable<SearchNode> {
        private long tag;
        private SearchNode prev;
        private double estimatedistance;
        private double totaldistance;


        SearchNode(long id, SearchNode sucessor, double est, double total) {
            prev = sucessor;
            tag = id;
            estimatedistance = est;
            totaldistance = total;
        }

        @Override
        public int compareTo(SearchNode o) {
            double ans = (this.totaldistance - o.totaldistance);

            if (ans > 0) {
                return 1;
            } else if (ans < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static LinkedList<Long> shortestPath(GraphDB g, double stlon,
                                                double stlat, double destlon, double destlat) {
        long srcID = g.closest(stlon, stlat);
        long dstID = g.closest(destlon, destlat);
        double edis = g.distance(srcID, dstID);
        SearchNode init = new SearchNode(srcID, null, edis, 0);
        solution = spf(g, init, dstID);
        return solution;
    }

    private static LinkedList<Long> spf(GraphDB g, SearchNode init, long dstID) {
        PriorityQueue<SearchNode> pq = new PriorityQueue<>(SearchNode::compareTo);
        pq.add(init);
        SearchNode x = pq.remove();
        LinkedList<Long> sol = new LinkedList<>();
        HashSet<Long> seen = new HashSet<>();

        while (x.tag != dstID) {
            Iterable<Long> adj = g.db.get(x.tag).adj;
            for (long i : adj) {
                if ((x.prev == null || i != (x.prev.tag)) && !seen.contains(i)) {
                    pq.add(new SearchNode(i, x,
                            g.distance(x.tag, dstID),
                            x.totaldistance + g.distance(i, x.tag)));
                }
            }
            x = pq.remove();
            seen.add(x.tag);
        }
        sol.add(x.tag);
        while (x.prev != null) {
            sol.add(x.prev.tag);
            x = x.prev;
        }

        for (int i = 0, j = sol.size() - 1; i < j; i++) {
            sol.add(i, sol.remove(j));
        }
        return sol;
    }
}
