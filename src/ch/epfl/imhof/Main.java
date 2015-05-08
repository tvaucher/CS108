package ch.epfl.imhof;

import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.SwissPainter;
import ch.epfl.imhof.projection.CH1903Projection;

public class Main {
    private final static double INCHES_PER_METRE = 39.3700787; // in
                                                               // inches/metre
    private final static double BLURRING_RADIUS = 1.7; // in millimetres.

    public static void main(String[] args) {
        String mapName = args[0];
        String hgtName = args[1]; // TODO: Take care of HGT files

        // Coordinates will directly be stored in radians:
        Double blLongitude = Math.toRadians(Double.parseDouble(args[2]));
        Double blLatitude = Math.toRadians(Double.parseDouble(args[3]));
        Double trLongitude = Math.toRadians(Double.parseDouble(args[4]));
        Double trLatitude = Math.toRadians(Double.parseDouble(args[5]));
        int dpi = Integer.parseInt(args[6]);
        String outputName = args[7];

        // Now, convert the input to metrics that we can use:
        CH1903Projection projector = new CH1903Projection();
        Point bl = projector.project(new PointGeo(blLongitude, blLatitude));
        Point tr = projector.project(new PointGeo(trLongitude, trLatitude));
        double resolution = dpi * INCHES_PER_METRE;
        int height = (int) Math.round(resolution * (1.0 / 25000)
                * (trLatitude - blLatitude) * Earth.RADIUS);
        int width = (int) Math.round(height * (tr.x() - bl.x())
                / (tr.y() - bl.y()));

        Painter painter = SwissPainter.painter();
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(
                new CH1903Projection());
        OSMMap osmMap = null;

        try {
            osmMap = OSMMapReader.readOSMFile("data/OSM/" + mapName, true);
        } catch (Exception e) {
            System.out.println("An error occured while reading the file.");
            e.printStackTrace();
        }

        Map map = transformer.transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, dpi,
                Color.WHITE);
        painter.drawMap(map, canvas);
        try {
            ImageIO.write(canvas.image(), "png", new File("data/image/"
                    + outputName));
        } catch (Exception e) {
            System.out.println("An error occured while writing the file.");
            e.printStackTrace();
        }
    }
}
