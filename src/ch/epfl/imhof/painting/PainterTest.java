package ch.epfl.imhof.painting;

import java.awt.image.BufferedImage;
import java.io.File;
//import java.util.function.Predicate;

import javax.imageio.ImageIO;

//import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
//import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public class PainterTest {
    public static void main(String[] args) {
        // Le peintre et ses filtres
        /*Predicate<Attributed<?>> isLake =
            Filters.tagged("natural", "water");
        Painter lakesPainter =
            Painter.polygon(Color.BLUE).when(isLake);
                
        Predicate<Attributed<?>> isForest =
            Filters.tagged("landuse", "forest");
        Painter forestPainter =
            Painter.polygon(Color.GREEN).when(isForest);
        
        Predicate<Attributed<?>> isBeach =
                Filters.tagged("natural", "beach");
        Painter beachPainter =
            Painter.polygon(Color.rgb(1, 1, 0)).when(isBeach);
        
        Predicate<Attributed<?>> isBuilding =
            Filters.tagged("building");
        Painter buildingsPainter =
            Painter.polygon(Color.BLACK).when(isBuilding);
        
        Painter restPainter = Painter.line(0.5f, Color.gray(0.7)).layered();

        Painter painter = buildingsPainter.above(restPainter).above(forestPainter).above(beachPainter).above(lakesPainter);*/
        Painter painter = SwissPainter.painter();
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(new CH1903Projection());
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/OSM/lausanne.osm.gz", true); 
        }
        catch (Exception e) {
            System.out.println("unsuccessful reading");
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap); // Lue depuis lausanne.osm.gz*/

        // La toile

        // INTERLAKEN:
        //Point bl = new Point(628590, 168210);
        //Point tr = new Point(635660, 172870);
        
        // BERNE: 
        //Point bl = new Point(597475, 197590);
        //Point tr = new Point(605705, 203363);
        
        // CPH:
        // PointGeo a = new PointGeo(Math.toRadians(12.5747), Math.toRadians(55.6730));
        // PointGeo b = new PointGeo(Math.toRadians(12.6421), Math.toRadians(55.6939));
        // CH1903Projection projector = new CH1903Projection();
        // Point bl = projector.project(a);
        // Point tr = projector.project(b);
        
        // LAUSANNE:
        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);

        // VALAIS:
        PointGeo blGeo = new PointGeo(Math.toRadians(7.2), Math.toRadians(46.2));
        PointGeo trGeo = new PointGeo(Math.toRadians(7.8), Math.toRadians(46.8));
        //System.out.println(blGeo.latitude() + " " + blGeo.longitude() + " " + trGeo.latitude() + " " + trGeo.longitude());
        
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800, 530, 150, Color.WHITE);
        try (HGTDigitalElevationModel model = new HGTDigitalElevationModel(new File("data/HGT/N46E006.hgt"))) {
            //System.out.println(proj.inverse(proj.project(blGeo)).longitude());
            ReliefShader rel = new ReliefShader(new CH1903Projection(), model, new Vector3(-1, 1, 1));   
            BufferedImage relief = rel.shadedRelief(bl, tr, 800, 530, 5);
            painter.drawMap(map, canvas);
            for (int i = 0; i < relief.getWidth(); ++i) {
                for (int j = 0; j < relief.getHeight(); ++j) {
                    Color mixed = Color.rgb(canvas.image().getRGB(i, j)).multiplyWith(Color.rgb(relief.getRGB(i, j)));
                    relief.setRGB(i, j, mixed.packedRBG());
                }
            }
            ImageIO.write(relief, "png", new File("data/image/LausanneOut.png"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        //BW MAP
        /*canvas.drawBWMap(new File("data/HGT/N46E007.hgt"), blGeo, trGeo);
        try {
            
            ImageIO.write(canvas.image(), "png", new File("data/image/BWtest2.png"));
        }
        catch (Exception e) {
            System.out.println("unsuccessful writing");
            e.printStackTrace();
        }*/
    }
}
