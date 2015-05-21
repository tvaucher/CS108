package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Pattern;

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
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.utility.QueryGenerator;

/**
 * The Main class is what ties everything else together. It's the main
 * orchestrator of the rest of the code, which manages inputs and outputs.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public class Main {
    private final static double INCHES_PER_METRE = 39.3700787; // in
                                                               // inches/metre
    private final static double BLUR_RADIUS = 1.7; // in millimetres.
    private final static Pattern gzPattern = Pattern.compile(".+\\.gz$");
    private final static Vector3 LIGHT_VECTOR = new Vector3(-1, 1, 1);

    // @formatter:off
    /**
     * The method to execute to create a map.
     * 
     * @param args
     *            It takes a list of 8 strings: 
     *            - The name of the .osm or .osm.gz file 
     *            - The name of the .hgt file
     *            - The longitude of the bottom left point
     *            - The latitude of the bottom left point
     *            - The longitude of the top right point
     *            - The latitude of the top right point
     *            - The resolution (in dpi) of the map
     *            - The name of the outputted file.
     * @throws IllegalArgumentException
     *             if there are more or less than 8 arguments.
     */
    // @formatter:on
    public static void main(String[] args) {
        if (args.length != 8)
            throw new IllegalArgumentException(
                    "Le nombre d'arguments fourni est incorrect.\n  Nombre d'arguments attendus: 8\n  Nombre d'arguments donnés: "
                            + args.length);

        String string = args[3] + " " + args[2] + " " + args[5] + " " + args[4];
        QueryGenerator qg = new QueryGenerator(string);
        Point bl = qg.bl();
        //System.out.println("bl : " + Math.toDegrees(bl.latitude()) + ", " + Math.toDegrees(bl.longitude()));
        Point tr = qg.tr();
        //System.out.println("tr : " + Math.toDegrees(tr.latitude()) + ", " + Math.toDegrees(tr.longitude()));
        
        String urlOsm = qg.getURLosm();
        String urlHgt = qg.getURLhgt();
        
        //String mapName = args[0];
        //String hgtName = args[1];

        // Coordinates will directly be stored in radians:
        /*double blLongitude = Math.toRadians(Double.parseDouble(args[2]));
        double blLatitude = Math.toRadians(Double.parseDouble(args[3]));
        double trLongitude = Math.toRadians(Double.parseDouble(args[4]));
        double trLatitude = Math.toRadians(Double.parseDouble(args[5]));*/
        int dpi = Integer.parseInt(args[6]);
        String outputName = args[7];

        
        // Now, convert the input to metrics that we can use:
        Projection projector = new CH1903Projection();
        /*Point bl = projector.project(blGeo);
        Point tr = projector.project(trGeo);*/

        double resolution = dpi * INCHES_PER_METRE;
        double blur = (resolution * BLUR_RADIUS) / (1000d); // in pixels
        int height = (int) Math.round(resolution * (1d / 25000)
                * qg.getDiff() * Earth.RADIUS);
        int width = (int) Math.round(height * (tr.x() - bl.x())
                / (tr.y() - bl.y()));

        Painter painter = SwissPainter.painter();
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projector);
        OSMMap osmMap = null;

        try {
            osmMap = OSMMapReader.readOSMFile(urlOsm);

        } catch (Exception e) {
            System.out.println("An error occured while reading the file.");
            e.printStackTrace();
        }

        Map map = transformer.transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, dpi,
                Color.WHITE);
        try (HGTDigitalElevationModel model = new HGTDigitalElevationModel(urlHgt);) {
            ReliefShader rel = new ReliefShader(projector, model, LIGHT_VECTOR);
            BufferedImage relief = rel
                    .shadedRelief(bl, tr, width, height, blur);
            painter.drawMap(map, canvas);
            ImageIO.write(mix(canvas.image(), relief), "png", new File(
                    outputName));
        } catch (Exception e) {
            System.out.println("An error occured while writing the file.");
            e.printStackTrace();
        }
    }

    private static BufferedImage mix(BufferedImage i1, BufferedImage i2) {
        BufferedImage out = new BufferedImage(i1.getWidth(), i1.getHeight(),
                i1.getType());
        for (int i = 0; i < i1.getWidth(); ++i) {
            for (int j = 0; j < i1.getHeight(); ++j) {
                Color mixed = Color.rgb(i1.getRGB(i, j)).multiplyWith(
                        Color.rgb(i2.getRGB(i, j)));
                out.setRGB(i, j, mixed.packedRBG());
            }
        }
        return out;
    }
}
