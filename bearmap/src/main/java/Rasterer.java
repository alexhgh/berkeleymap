import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private QuadTree qtree;
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.

    /**
     * imgRoot is the name of the directory containing the images.
     * You may not actually need this for your class.
     */
    public Rasterer(String imgRoot) {
        qtree = new QuadTree();
        qtree.insert(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON,
                MapServer.ROOT_LRLAT, imgRoot + "root");

        for (int i = 1; i <= 7; i++) {
            insertimage(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON,
                    MapServer.ROOT_LRLAT, 0, "", i);
        }
    }

    private void insertimage(double xx, double xy, double yx, double yy, int counter,
                             String curr, int target) {
        if (counter == target) {
            qtree.insert(xx, xy, yx, yy, curr);
        } else {
            insertimage(xx, xy, (xx + yx) / 2, (yy + xy) / 2, counter + 1, curr + "1",
                    target);

            insertimage((xx + yx) / 2, xy, yx, (yy + xy) / 2, counter + 1, curr + "2",
                    target);

            insertimage(xx, (xy + yy) / 2, (xx + yx) / 2, yy, counter + 1, curr + "3",
                    target);

            insertimage((xx + yx) / 2, (yy + xy) / 2, yx, yy, counter + 1, curr + "4",
                    target);
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * <<<<<<< HEAD
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     * <li>Has dimensions of at least w by h, where w and h are the user viewport width
     * and height.</li>
     * <li>The tiles collected must cover the most longitudinal distance per pixel
     * (LonDPP) possible, while still covering less than or equal to the amount of
     * longitudinal distance per pixel in the query box for the user viewport size. </li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the
     * above condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     * =======
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     * <li>The tiles collected must cover the most longitudinal distance per pixel
     * (LonDPP) possible, while still covering less than or equal to the amount of
     * longitudinal distance per pixel in the query box for the user viewport size. </li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the
     * above condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     * >>>>>>> e5eb1deae982c391e100a8fe3837e97db9d60f20
     * </p>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     * Can also be interpreted as the length of the numbers in the image
     * string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     * forget to set this to true! <br>
     * @see #"REQUIRED_RASTER_REQUEST_PARAMS"
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();

        double lowerX = params.get("ullon");
        double upperX = params.get("lrlon");
        double lowerY = params.get("ullat");
        double upperY = params.get("lrlat");
        double boxW = params.get("w");
        double lonDPP = Math.abs((upperX - lowerX) / (boxW));
        Line intervalX = new Line(lowerX, upperX);
        Line intervalY = new Line(lowerY, upperY);
        Box intervalXY = new Box(intervalX, intervalY);

        qtree.searching(intervalXY, boxW, lonDPP);
        ArrayList<DataBlock> s1 = qtree.getresult();
        //find row and col
        int col = 0;
        double height = (s1.get(0).getXY());
        for (int i = 0; i < s1.size() / 2 + 1; i++) {
            if ((s1.get(i).getXY()) == height) {
                col = col + 1;
            }
        }
        String[][] resultimg;
        resultimg = new String[s1.size() / col][col];
        for (int i = 0, k = 0; i < s1.size() / col; i++) {
            for (int j = 0; j < col; j++) {
                resultimg[i][j] = "img/"
                        + s1.get(k).getID() + ".png";
                k = k + 1;
            }
        }
        results.put("depth", (s1.get(0).getDepth()));
        results.put("raster_ul_lon", (s1.get(0).getX()));
        results.put("raster_ul_lat", (s1.get(0).getXY()));
        results.put("raster_lr_lon", (s1.get(s1.size() - 1).getYX()));
        results.put("raster_lr_lat", (s1.get(s1.size() - 1).getY()));
        results.put("query_success", true);
        results.put("render_grid", resultimg);
        return results;
    }

}
