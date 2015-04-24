package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Painter is a functional interface defining painters that describe which
 * elements from a map have to be painted on a canvas. It also proposes default
 * utility operators that filter/stack existing painters.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
@FunctionalInterface
public interface Painter {
    /**
     * Only abstract method of the functional interface. Apply the parameters to
     * a painter to allow drawing the map on the canvas.
     * 
     * @param map
     *            map that will be drawn on the canvas.
     * @param canvas
     *            canvas that will be drawn on.
     */
    public abstract void drawMap(Map map, Canvas canvas);

    /**
     * Basic painter that draws the inside of all the polygon it received with
     * the given color.
     * 
     * @param color
     *            color of the inside of the polygons.
     * @return a new painter with given parameters that draws polygons.
     */
    public static Painter polygon(Color color) {
        return (map, canvas) -> {
            for (Attributed<Polygon> polygon : map.polygons()) {
                canvas.drawPolygon(polygon.value(), color);
            }
        };
    }

    /**
     * Basic painter that draws all the polylines it received with the given
     * style.
     * 
     * @param style
     *            drawing style
     * @return a new painter with given parameters that draws polylines.
     */
    public static Painter line(LineStyle style) {
        return (map, canvas) -> {
            for (Attributed<PolyLine> polyLine : map.polyLines()) {
                canvas.drawPolyLine(polyLine.value(), style);
            }
        };
    }

    /**
     * Basic painter that draws all the polylines it received with the given
     * style, parameters by parameters @see {@link LineStyle}.
     * 
     * @param width
     *            width of stroke
     * @param color
     *            color of stroke
     * @param lineCap
     * @param lineJoin
     * @param dashingPattern
     * @return a new painter with given parameters that draws polylines.
     */
    public static Painter line(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float... dashingPattern) {
        return line(new LineStyle(width, color, lineCap, lineJoin,
                dashingPattern));
    }

    /**
     * Basic painter that draws all the polylines it received with the given
     * style, parameters by parameters @see {@link LineStyle}.
     * 
     * @param width
     *            width of stroke
     * @param color
     *            color of stroke
     * @return a new painter with given parameters that draws polylines.
     */
    public static Painter line(float width, Color color) {
        return line(new LineStyle(width, color));
    }

    /**
     * Basic painter that draws all the outline of the polylines it received
     * with the given style.
     * 
     * @param style
     *            drawing style
     * @return a new painter with given parameters that draws the outside of
     *         polygons.
     */
    public static Painter outline(LineStyle style) {
        return (map, canvas) -> {
            for (Attributed<Polygon> polygon : map.polygons()) {
                for (PolyLine polyline : polygon.value().holes())
                    canvas.drawPolyLine(polyline, style);
                canvas.drawPolyLine(polygon.value().shell(), style);
            }
        };
    }

    /**
     * Basic painter that draws all the outline of the polylines it received
     * with the given style, parameters by parameters @see {@link LineStyle}.
     * 
     * @param width
     *            width of stroke
     * @param color
     *            color of stroke
     * @param lineCap
     * @param lineJoin
     * @param dashingPattern
     * @return a new painter with given parameters that draws the outside of
     *         polygons.
     */
    public static Painter outline(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float... dashingPattern) {
        return outline(new LineStyle(width, color, lineCap, lineJoin,
                dashingPattern));
    }

    /**
     * Basic painter that draws all the outline of the polylines it received
     * with the given style, parameters by parameters @see {@link LineStyle}.
     * 
     * @param width
     *            width of stroke
     * @param color
     *            color of stroke
     * @return a new painter with given parameters that draws the outside of
     *         polygons.
     */
    public static Painter outline(float width, Color color) {
        return outline(new LineStyle(width, color));
    }

    /**
     * Empty painter that does nothing. Useful as a basis for stacking painters
     * (with above) so that we don't have null exception.
     * 
     * @return a painter that does nothing
     */
    public static Painter empty() {
        return (map, canvas) -> {
        };
    }

    /**
     * Derives a painter by adding a filter on the painter. So that the painter
     * only draws the elements that satisfy the predicates.
     * 
     * @param predicate
     *            predicate that has to be satisfied for an element to be drawn.
     * @return the derived painter
     */
    public default Painter when(Predicate<Attributed<?>> predicate) {
        return (map, canvas) -> {
            Map.Builder builder = new Map.Builder();
            for (Attributed<PolyLine> p : map.polyLines())
                if (predicate.test(p))
                    builder.addPolyLine(p);
            for (Attributed<Polygon> p : map.polygons())
                if (predicate.test(p))
                    builder.addPolygon(p);
            drawMap(builder.build(), canvas);
        };
    }

    /**
     * Derives a painter by adding that it has to be drawn above the painter
     * given in the parameters.
     * 
     * @param that
     *            painter that has to be drawn below current painter
     * @return new painter that paints current painter above given painter
     */
    public default Painter above(Painter that) {
        return (map, canvas) -> {
            that.drawMap(map, canvas);
            this.drawMap(map, canvas);
        };
    }

    /**
     * Derives a painter that draws the map layer by layer from -5 to 5.
     * 
     * @return new painter that draws the map layer by layer
     */
    public default Painter layered() {
        return (map, canvas) -> {
            Painter out = empty();
            for (int i = -5; i <= 5; ++i)
                out = when(Filters.onLayer(i)).above(out);
            out.drawMap(map, canvas);
        };
    }
}
