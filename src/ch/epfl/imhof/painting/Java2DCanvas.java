package ch.epfl.imhof.painting;

import java.util.function.Function;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * Java2DCanvas represents a concrete implementation of the canvas that we need
 * in order to draw the map in this project.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public class Java2DCanvas implements Canvas {
    private final BufferedImage image;
    private final Graphics2D ctx;

    private final Function<Point, Point> projectedToCanvas;
    private final static int CANVAS_DPI = 72;

    // private final double scalingFactor;

    /**
     * Constructs a new Java2D object.
     * 
     * @param bl
     *            The bottom-left Point of the Canvas.
     * @param tr
     *            The top-right Point of the Canvas (should be above and to the
     *            right of bl)
     * @param width
     *            The width of the Canvas, in pixels (should be >=0).
     * @param height
     *            The height of the Canvas, in pixels (should be >=0).
     * @param dpi
     *            The resolution of the Canvas image, in dots per inch (should
     *            be >=0).
     * @param bg
     *            The background Color of the Canvas.
     * 
     * @throws IllegalArgumentException
     *             If any of the parameters are outside of their allowed ranges,
     *             or are nonsensical (e.g. a bottom-left point that is above
     *             the top-right)
     */
    public Java2DCanvas(Point bl, Point tr, int width, int height, int dpi,
            Color bg) {
        if (height < 0 || width < 0)
            throw new IllegalArgumentException(
                    "Width and height must be bigger than 0");
        if (dpi < 0)
            throw new IllegalArgumentException(
                    "Resolution must be bigger than 0");
        if (bl.x() > tr.x() || bl.y() > tr.y())
            throw new IllegalArgumentException(
                    "The bottom left point must be below and to the left of the top right point");
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ctx = image.createGraphics();

        // Set antialiasing
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

        ctx.setColor(bg.toAWTColor());
        ctx.fillRect(0, 0, width, height);
        // Change the scale
        double scalingFactor = dpi * 1d / CANVAS_DPI;
        ctx.scale(scalingFactor, scalingFactor);
        projectedToCanvas = Point.alignedCoordinateChange(bl, new Point(0,
                height / scalingFactor), tr,
                new Point(width / scalingFactor, 0));
        // Set Background color
        // ctx.setBackground(bg.toAWTColor());
        // System.out.println(ctx.getBackground().getRGB() + "RGB : " +
        // ctx.getBackground().getRed() + " r " + ctx.getBackground().getGreen()
        // + " g " + ctx.getBackground().getBlue() + " b");
    }

    /**
     * A getter for the image on the canvas.
     * 
     * @return A BufferedImage object representing what has been drawn onto the
     *         canvas.
     */
    public BufferedImage image() {
        return image;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.painting.Canvas#drawPolyLine(ch.epfl.imhof.geometry.PolyLine
     * , ch.epfl.imhof.painting.LineStyle)
     */
    @Override
    public void drawPolyLine(PolyLine polyLine, LineStyle style) {
        // parameters for the stroke
        ctx.setColor(style.color().toAWTColor());
        BasicStroke stroke = style.dashingPattern().length == 0 ? new BasicStroke(
                style.width(), style.lineCap().ordinal(), style.lineJoin()
                        .ordinal(), 10f) : new BasicStroke(style.width(), style
                .lineCap().ordinal(), style.lineJoin().ordinal(), 10f,
                style.dashingPattern(), 0f);

        // creating the polygon
        ctx.draw(stroke.createStrokedShape(pathPolyLine(polyLine)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.painting.Canvas#drawPolygon(ch.epfl.imhof.geometry.Polygon,
     * ch.epfl.imhof.painting.Color)
     */
    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        ctx.setColor(color.toAWTColor());
        // creating the shell
        Area shell = new Area(pathPolyLine(polygon.shell()));
        for (PolyLine p : polygon.holes()) {
            shell.subtract(new Area(pathPolyLine(p)));
        }
        ctx.fill(shell);
    }

    private Shape pathPolyLine(PolyLine polyLine) {
        boolean first = true;
        Path2D.Double path = new Path2D.Double();
        for (Point point : polyLine.points()) {
            Point p = projectedToCanvas.apply(point);
            if (first) {
                path.moveTo(p.x(), p.y());
                first = false;
            } else
                path.lineTo(p.x(), p.y());
        }
        if (polyLine.isClosed())
            path.closePath();
        return path;
    }

}
