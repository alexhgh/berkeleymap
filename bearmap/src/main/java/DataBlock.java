/**
 * Created by hogun on 4/18/2017.
 */
public class DataBlock {
    private String tag;
    private double xx;
    private double xy;
    private double yx;
    private double yy;
    private int depth;
    private double metric;

    public DataBlock(String t, double x, double xy, double yx, double y, int depth) {
        tag = t;
        xx = x;
        this.xy = xy;
        this.yx = yx;
        yy = y;
        this.depth = depth;
        metric = xy * -1000000 + x;
    }

    public double getX(){
        return xx;
    }

    public double getXY(){
        return xy;
    }

    public double getYX(){
        return yx;
    }

    public double getY(){
        return yy;
    }

    public String getID() {
        return tag;
    }

    public int getDepth() {
        return depth;
    }

    public double getMetric() {
        return metric;
    }
}
