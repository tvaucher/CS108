package ch.epfl.imhof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.testUtilities.ListNonMutableTestUtility;

public class MapTest {

    private HashMap<String, String> sampleAttributesValues() {
        HashMap<String, String> testData = new HashMap<>();
        testData.put("testKey 1", "testValue 1");
        testData.put("testKey 2", "testValue 2");
        testData.put("testKey 3", "testValue 3");
        return testData;
    }

    @Test
    public void constructorAndPolylinesListNonModifiable() {
        List<Attributed<PolyLine>> testAttributedPolyLineList = new ArrayList<>();
        List<Point> testPolyLine1PointList = new ArrayList<>();
        Point testPoint1 = new Point(0.125621, 0.803253);
        testPolyLine1PointList.add(testPoint1);
        Point testPoint2 = new Point(0.110567, 0.801039);
        testPolyLine1PointList.add(testPoint2);
        Point testPoint3 = new Point(0.130156, 0.806719);
        testPolyLine1PointList.add(testPoint3);
        ClosedPolyLine testPolyLine1 = new ClosedPolyLine(testPolyLine1PointList);
        Attributes testAttributes1 = new Attributes( sampleAttributesValues() );
        Attributed<PolyLine> testAttributedPolyLine1 = new Attributed<>(testPolyLine1, testAttributes1);
        testAttributedPolyLineList.add(testAttributedPolyLine1);
        List<Point> testPolyLine2PointList = new ArrayList<>();
        Point testPoint4 = new Point(0.124291, 0.802579);
        testPolyLine2PointList.add(testPoint4);
        Point testPoint5 = new Point(0.109754, 0.801612);
        testPolyLine2PointList.add(testPoint5);
        Point testPoint6 = new Point(0.110721, 0.810231);
        testPolyLine2PointList.add(testPoint6);
        ClosedPolyLine testPolyLine2 = new ClosedPolyLine(testPolyLine2PointList);
        Attributes testAttributes2 = new Attributes( sampleAttributesValues() );
        Attributed<PolyLine> testAttributedPolyLine2 = new Attributed<>(testPolyLine2, testAttributes2);
        testAttributedPolyLineList.add(testAttributedPolyLine2);
        Map testMap = new Map(testAttributedPolyLineList, new ArrayList<Attributed<Polygon>>());
        assertTrue(ListNonMutableTestUtility.nonMutableFieldListTest(testAttributedPolyLineList, testMap.polyLines()));
        }

    @Test
    public void constructorAndPolygonsListNonModifiable() {
        List<Attributed<Polygon>> testAttributedPolygonList = new ArrayList<>();
        List<Point> testPolygon1ShellPointList = new ArrayList<>();
        Point testPoint1 = new Point(0.125621, 0.803253);
        testPolygon1ShellPointList.add(testPoint1);
        Point testPoint2 = new Point(0.110567, 0.801039);
        testPolygon1ShellPointList.add(testPoint2);
        Point testPoint3 = new Point(0.130156, 0.806719);
        testPolygon1ShellPointList.add(testPoint3);
        ClosedPolyLine testPolygon1Shell = new ClosedPolyLine(testPolygon1ShellPointList);
        Polygon testPolygon1 = new Polygon(testPolygon1Shell);
        Attributes testAttributes1 = new Attributes( sampleAttributesValues() );
        Attributed<Polygon> testAttributedPolygon1 = new Attributed<>(testPolygon1, testAttributes1);
        testAttributedPolygonList.add(testAttributedPolygon1);
        List<Point> testPolygon2ShellPointList = new ArrayList<>();
        Point testPoint4 = new Point(0.124291, 0.802579);
        testPolygon2ShellPointList.add(testPoint4);
        Point testPoint5 = new Point(0.109754, 0.801612);
        testPolygon2ShellPointList.add(testPoint5);
        Point testPoint6 = new Point(0.110721, 0.810231);
        testPolygon2ShellPointList.add(testPoint6);
        ClosedPolyLine testPolygon2Shell = new ClosedPolyLine(testPolygon2ShellPointList);
        List<ClosedPolyLine> testPolygon2HolesList = new ArrayList<>();
        List<Point> testPolygon2Hole1PointList = new ArrayList<>();
        Point testPoint7 = new Point(0.121372, 0.803053);
        testPolygon2Hole1PointList.add(testPoint7);
        Point testPoint8 = new Point(0.112691, 0.802201);
        testPolygon2Hole1PointList.add(testPoint8);
        Point testPoint9 = new Point(0.111121, 0.806132);
        testPolygon2Hole1PointList.add(testPoint9);
        ClosedPolyLine testPolygon2Hole1 = new ClosedPolyLine(testPolygon2Hole1PointList);
        testPolygon2HolesList.add(testPolygon2Hole1);
        Polygon testPolygon2 = new Polygon(testPolygon2Shell, testPolygon2HolesList);
        Attributes testAttributes2 = new Attributes( sampleAttributesValues() );
        Attributed<Polygon> testAttributedPolygon2 = new Attributed<>(testPolygon2, testAttributes2);
        testAttributedPolygonList.add(testAttributedPolygon2);
        Map testMap = new Map(new ArrayList<Attributed<PolyLine>>(), testAttributedPolygonList);
        assertTrue(ListNonMutableTestUtility.nonMutableFieldListTest(testAttributedPolygonList, testMap.polygons()));
        }
    
    @Test
    public void builder() {
        Attributes attributes = new Attributes(sampleAttributesValues());
        PolyLine.Builder polyLineBuilder = new PolyLine.Builder();
        polyLineBuilder.addPoint(new Point(0.123456, 0.654321));
        polyLineBuilder.addPoint(new Point(0.123123, 0.654654));
        polyLineBuilder.addPoint(new Point(0.242452, 0.912315));
        Attributed<PolyLine> attributedPolyLine = new Attributed<PolyLine>(
                polyLineBuilder.buildClosed(), attributes);
        
        PolyLine.Builder shellBuilder = new PolyLine.Builder();
        shellBuilder.addPoint(new Point(0.121372, 0.803053));
        shellBuilder.addPoint(new Point(0.112691, 0.802201));
        shellBuilder.addPoint(new Point(0.111121, 0.806132));
        PolyLine.Builder holeBuilder = new PolyLine.Builder();
        holeBuilder.addPoint(new Point(0.121372, 0.803053));
        holeBuilder.addPoint(new Point(0.112691, 0.802201));
        holeBuilder.addPoint(new Point(0.111121, 0.806132));
        Polygon polygon = new Polygon(shellBuilder.buildClosed(),
                Arrays.asList(holeBuilder.buildClosed()));
        Attributed<Polygon> attributedPolygon = new Attributed<>(polygon, attributes);
        
        Map.Builder mapBuilder = new Map.Builder();
        mapBuilder.addPolyLine(attributedPolyLine);
        mapBuilder.addPolygon(attributedPolygon);
        Map mapWithBuilder = mapBuilder.build();
        
        Map mapWithoutBuilder = new Map(Arrays.asList(attributedPolyLine),Arrays.asList(attributedPolygon));
        assertEquals(mapWithoutBuilder.polygons(), mapWithBuilder.polygons());
        assertEquals(mapWithoutBuilder.polyLines(), mapWithBuilder.polyLines());
    }

}
