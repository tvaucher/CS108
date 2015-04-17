package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

@FunctionalInterface
public interface Painter {
    public abstract void drawMap(Map map, Canvas canvas);
    
    public static Painter polygon(Color color) {
        return (map, canvas) -> {
            for (Attributed<Polygon> polygon : map.polygons()) {
                canvas.drawPolygon(polygon.value(), color);
            }
        };
    }
    
    public static Painter line(float width, Color color, LineCap lineCap, LineJoin lineJoin, float[] dashingPattern) {
        LineStyle style = new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
        return (map, canvas) -> {
            for (Attributed<PolyLine> polyLine : map.polyLines()) {
                canvas.drawPolyLine(polyLine.value(), style);
            }
        };
    }
    
    public static Painter line(float width, Color color) {
        LineStyle style = new LineStyle(width, color);
        return (map, canvas) -> {
            for (Attributed<PolyLine> polyLine : map.polyLines()) {
                canvas.drawPolyLine(polyLine.value(), style);
            }
        };
    }
    
    public static Painter outline(float width, Color color, LineCap lineCap, LineJoin lineJoin, float[] dashingPattern) {
        LineStyle style = new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
        return (map, canvas) -> {
            for (Attributed<Polygon> polygon : map.polygons()) {
                canvas.drawPolyLine(polygon.value().shell(), style);
            }
        };
    }
    
    public static Painter outline(float width, Color color) {
        LineStyle style = new LineStyle(width, color);
        return (map, canvas) -> {
            for (Attributed<Polygon> polygon : map.polygons()) {
                canvas.drawPolyLine(polygon.value().shell(), style);
            }
        };
    }
    
    public default Painter when(Predicate<Attributed<?>> predicate) {
        return (map, canvas) -> {
            // TODO: Figure something out here!
        };
    }
}
