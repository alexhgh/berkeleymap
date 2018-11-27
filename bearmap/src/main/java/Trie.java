import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Trie {
    private Node root;

    private class Node implements Comparable<Node> {
        char c;
        LinkedHashMap<Character, Node> branch = new LinkedHashMap<>();
        boolean isLeaf;
        List<Location> sol = new LinkedList<>();
        List<String> possibleQuery = new ArrayList<>();

        //default constructor
        Node() {
        }
        //the constructor we use for node
        Node(char c) {
            this.c = c;
        }

        @Override
        public int compareTo(Node o) {
            return this.c - o.c;
        }
    }

    public Trie() {
        root = new Node();
    }

    // Inserts a shop.
    public void insert(String word, long id, double x, double y, String realname) {
        LinkedHashMap<Character, Node> branch = root.branch;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Node t;
            if (branch.containsKey(c)) {
                t = branch.get(c);
            } else {
                t = new Node(c);
                branch.put(c, t);
            }
            branch = t.branch;
            if (i == word.length() - 1) {
                t.isLeaf = true;
                Location l = new Location(realname, id, x, y);
                t.sol.add(l);
            }
        }
    }

    public List<String> startsWith(String pre) {
        LinkedHashMap<Character, Node> branch = root.branch;
        Node t = null;
        for (int i = 0; i < pre.length(); i++) {
            char c = pre.charAt(i);
            if (branch.containsKey(c)) {
                t = branch.get(c);
                branch = t.branch;
            } else {
                return null;
            }
        }
        //Already acquired the tree
        //Next: use helper to add it to the that subtree result and return that subtree result
        //First par: is the ref node, Second par: is the candidate
        explorer(t, t);
        return t.possibleQuery;
    }

    private void explorer(Node t, Node b) {
        if (b == null) {
            return;
        } else if (b.isLeaf) {
            t.possibleQuery.add((String) b.sol.get(0).getMap().get("name"));
        }
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Node branch : b.branch.values()) {
            pq.add(branch);
        }
        while (!pq.isEmpty()) {
            explorer(t, pq.remove());
        }
    }

    public List<Location> searchNode(String x) {
        LinkedHashMap<Character, Node> branch = root.branch;
        Node t = null;
        for (int i = 0; i < x.length(); i++) {
            char c = x.charAt(i);
            if (branch.containsKey(c)) {
                t = branch.get(c);
                branch = t.branch;
            } else {
                return null;
            }
        }
        return t.sol;
    }
}
