package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
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
    private final static double BLUR_RADIUS = 1.7; // in millimetres.

    public static void main(String[] args) {
        String mapName = args[0];
        String hgtName = args[1];

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
        double blur = (resolution * BLUR_RADIUS) / (1000d); // in pixels
        int height = (int) Math.round(resolution * (1d / 25000)
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
        try (HGTDigitalElevationModel model = new HGTDigitalElevationModel(
                new File("data/HGT/" + hgtName))) {
            ReliefShader rel = new ReliefShader(new CH1903Projection(), model,
                    new Vector3(-1, 1, 1));
            BufferedImage relief = rel
                    .shadedRelief(bl, tr, width, height, blur);
            painter.drawMap(map, canvas);
            for (int i = 0; i < relief.getWidth(); ++i) {
                for (int j = 0; j < relief.getHeight(); ++j) {
                    Color mixed = Color.rgb(canvas.image().getRGB(i, j))
                            .multiplyWith(Color.rgb(relief.getRGB(i, j)));
                    relief.setRGB(i, j, mixed.packedRBG());
                }
            }
            ImageIO.write(relief, "png", new File("data/image/" + outputName));
        } catch (Exception e) {
            System.out.println("An error occured while writing the file.");
            e.printStackTrace();
        }
    }
}
