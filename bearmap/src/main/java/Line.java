public class Line {
    private double mi;
    private double ma;

    public Line(double mi, double ma) {
        this.mi = Math.min(mi, ma);
        this.ma = Math.max(mi, ma);
    }

    public double min() {
        return mi;
    }

    public double max() {
        return ma;
    }

    public boolean intersects(Line x) {
        if (this.ma < x.mi || x.ma < this.mi) {
            return false;
        }
        return true;
    }
}
