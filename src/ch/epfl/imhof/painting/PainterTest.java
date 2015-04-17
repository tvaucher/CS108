package ch.epfl.imhof.painting;

import java.io.File;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public class PainterTest {
    public static void main(String[] args) {
        // Le peintre et ses filtres
        Predicate<Attributed<?>> isLake =
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

        Painter painter = buildingsPainter.above(restPainter).above(forestPainter).above(beachPainter).above(lakesPainter);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(new CH1903Projection());
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/lausanne.osm.gz", true); 
        }
        catch (Exception e) {
            System.out.println("unsuccessful reading");
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap); // Lue depuis lausanne.osm.gz

        // La toile
        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);
        Java2DCanvas canvas =
            new Java2DCanvas(bl, tr, 800, 530, 72, Color.rgb(0xD8EDD8));

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(map, canvas);
        try {
            ImageIO.write(canvas.image(), "png", new File("loz.png"));
        }
        catch (Exception e) {
            System.out.println("unsuccessful writing");
            e.printStackTrace();
        }
    }
}
