import java.util.ArrayList;
import java.util.Comparator;

public class QuadTree {

    private Node root;
    private ArrayList<DataBlock> result;

    public class Node {
        double x, xy, y, yx, xmidpt, ymidpt;
        Node NW, NE, SE, SW;
        String value;

        Node(double x, double xy, double yx, double y, String value) {
            this.x = x;
            this.y = y;
            this.xy = xy;
            this.yx = yx;
            this.value = value;
            xmidpt = (yx + x) / 2;
            ymidpt = (xy + y) / 2;
        }
    }

    public void insert(double x, double xy, double yx, double y, String value) {
        root = insert(root, root, x, xy, yx, y, value, "");
    }

    private Node insert(Node h, Node prev, double x, double xy, double yx,
                        double y, String value, String dir) {
        if (h == null) {
            if (dir.equals("NW")) {
                prev.NW = new Node(x, xy, yx, y, value);
                return prev.NW;
            } else if (dir.equals("NE")) {
                prev.NE = new Node(x, xy, yx, y, value);
                return prev.NE;
            } else if (dir.equals("SW")) {
                prev.SW = new Node(x, xy, yx, y, value);
                return prev.SW;
            } else if (dir.equals("SE")) {
                prev.SE = new Node(x, xy, yx, y, value);
                return prev.SE;
            } else {
                return new Node(x, xy, yx, y, value);
            }

        } else if ((x - h.xmidpt < 0) && (y - h.ymidpt >= 0)) {
            insert(h.NW, h, x, xy, yx, y, value, "NW");
        } else if ((x - h.xmidpt >= 0) && (y - h.ymidpt >= 0)) {
            insert(h.NE, h, x, xy, yx, y, value, "NE");
        } else if ((x - h.xmidpt < 0) && (y - h.ymidpt < 0)) {
            insert(h.SW, h, x, xy, yx, y, value, "SW");
        } else if ((x - h.xmidpt >= 0) && (y - h.ymidpt < 0)) {
            insert(h.SE, h, x, xy, yx, y, value, "SE");
        }
        return h;
    }

    public void searching(Box rect, double boxW, double comB) {
        double box = boxW;
        double compareDDP = comB;
        result = new ArrayList<>();
        searching(root, rect, 0, box, compareDDP);
    }

    private void searching(Node h, Box box, int de, double boxW, double desiredDDP) {
        if (de == 7) {
            DataBlock datum = new DataBlock(h.value, h.x, h.xy, h.yx, h.y, 7);
            result.add(datum);

            return;
        }
        //find interval xy
        double boxXmin = box.intervalX.min();
        double boxXmax = box.intervalX.max();
        double boxYmax = box.intervalY.max();
        double boxYmin = box.intervalY.min();
        double pictureDDP = Math.abs((h.yx - h.x)) / (256);

        Line nodeXinterval = new Line(h.x, h.yx);
        Line nodeYinterval = new Line(h.y, h.xy);

        if (box.intercept(nodeXinterval, nodeYinterval) && pictureDDP <= desiredDDP) {
            DataBlock datum = new DataBlock(h.value, h.x, h.xy, h.yx, h.y, de);
            result.add(datum);

            return;
        }
        if ((boxXmin - h.xmidpt < 0) && (boxYmax - h.ymidpt >= 0)) {
            searching(h.NW, box, de + 1, boxW, desiredDDP);
        }
        if ((boxXmax - h.xmidpt >= 0) && (boxYmax - h.ymidpt >= 0)) {
            searching(h.NE, box, de + 1, boxW, desiredDDP);
        }
        if ((boxXmin - h.xmidpt < 0) && (boxYmin - h.ymidpt < 0)) {
            searching(h.SW, box, de + 1, boxW, desiredDDP);
        }
        if ((boxXmax - h.xmidpt >= 0) && (boxYmin - h.ymidpt < 0)) {
            searching(h.SE, box, de + 1, boxW, desiredDDP);
        }
    }

    public ArrayList<DataBlock> getresult() {
        result.sort(Comparator.comparing(a -> a.getMetric()));
        return result;
    }
}
