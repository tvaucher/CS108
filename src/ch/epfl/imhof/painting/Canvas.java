package ch.epfl.imhof.painting;

import java.awt.Font;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Canvas is a simple interface containing two abstract methods, since all
 * canvases must be able to draw polygons and polylines as a part of this
 * project.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public interface Canvas {
    /**
     * A method that draws a PolyLine onto the canvas.
     * 
     * @param polyLine
     *            The PolyLine that will be drawn onto the canvas.
     * @param style
     *            The LineStyle of said PolyLine.
     */
    public void drawPolyLine(PolyLine polyLine, LineStyle style);

    /**
     * A method that draws a Polygon onto the canvas, filling it with the given
     * Color.
     * 
     * @param polygon
     *            The Polygon that will be drawn onto the canvas.
     * @param color
     *            The Color that will be used to fill said Polygon.
     */
    public void drawPolygon(Polygon polygon, Color color);

    /**
     * A method that draws a Polygon onto the canvas, filling it with the given
     * texture
     * 
     * @param polygon
     *            The Polygon that will be drawn onto the canvas.
     * @param texture
     *            The name of texture that will be used to fill said Polygon.
     */
    public void drawPolygon(Polygon polygon, String texture);

    /**
     * A method that draws a PlaceName onto the canvas.
     * 
     * @param point
     *            Position of place
     * @param name
     *            Name of place
     * @param font
     *            font to draw
     * @param color
     *            color of the text
     */
    public void drawPlace(Point point, String name, Font font, Color color);
}
