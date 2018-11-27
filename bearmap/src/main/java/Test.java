public class Test {
    private QuadTree qtree = new QuadTree();

    /**
     * imgRoot is the name of the directory containing the images.
     * You may not actually need this for your class.
     */
    public Test(String imgRoot) {
        qtree.insert(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON,
                MapServer.ROOT_LRLAT, "root");

        for (int i = 1; i <= 7; i++) {
            insertimage(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON,
                    MapServer.ROOT_LRLAT, 0, "img/", i);
        }
    }

    private void insertimage(double xx, double xy, double yx, double yy, int counter,
                             String curr, int target) {
        if (counter == target) {
            qtree.insert(xx, xy, yx, yy, curr);
            System.out.println("xx: " + xx + " xy: " + xy
                    + " yx: " + yx + " yy: " + yy + " Name: " + curr);
        } else {
            insertimage(xx, xy, (xx + yx) / 2, (yy + xy) / 2, counter + 1, curr + "1",
                    target);

            insertimage((xx + yx) / 2, xy, yx, (yy + xy) / 2, counter + 1, curr + "2",
                    target);

            insertimage(xx, (xy + yy) / 2, (xx + yx) / 2, yy, counter + 1, curr + "30",
                    target);

            insertimage((xx + yx) / 2, (yy + xy) / 2, yx, yy, counter + 1, curr + "40",
                    target);
        }
    }

    public static void main(String[] args) {
        Test t = new Test("root");

        Line intervalX = new Line(-122.3027284165759, -122.20908713544797);
        Line intervalY = new Line(37.88708748276975, 37.848731523430196);
        Box intervalXY = new Box(intervalX, intervalY);

        double comb = (Math.abs(intervalX.min() - intervalX.max())) / 305.0;

        t.qtree.searching(intervalXY, 305.0, comb);
    }
}
