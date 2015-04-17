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

public class Java2DCanvas implements Canvas {
    private final BufferedImage image;
    private final Graphics2D ctx;
    
    private final Function<Point, Point> projectedToCanvas;
    private final static int CANVAS_DPI = 72;
    //private final double scalingFactor;
    
    public Java2DCanvas(Point bl, Point tr, int width, int height, int dpi, Color bg) {
        if (height < 0 || width < 0)
            throw new IllegalArgumentException("width and heght must be bigger than 0");
        if (dpi < 0)
            throw new IllegalArgumentException("resolution must be bigger than 0");
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ctx = image.createGraphics();
        
        //Set antialiasing
        ctx.setRenderingHint(KEY_ANTIALIASING,
                VALUE_ANTIALIAS_ON);
        
        //Set Background color
        ctx.setBackground(bg.toAWTColor());
        
        //Change the scale
        double scalingFactor = dpi*1d/CANVAS_DPI;
        ctx.scale(scalingFactor, scalingFactor);
        projectedToCanvas = Point.alignedCoordinateChange(bl, new Point(0, height*scalingFactor), tr, new Point(width*scalingFactor, 0));
    }
    
    public BufferedImage image() { return image; }
    
    @Override
    public void drawPolyLine(PolyLine polyLine, LineStyle style) {
        int cap = 0, join = 0;
        
        //Generates cap style for BasicStroke type
        switch (style.lineCap()) {
        case BUTT :
            cap = BasicStroke.CAP_BUTT;
            break;
        case ROUND :
            cap = BasicStroke.CAP_ROUND;
            break;
        case SQUARE :
            cap = BasicStroke.CAP_SQUARE;
            break;
        }
        
        //Generates join style for BasicStroke type
        switch (style.lineJoin()) {
        case BEVEL :
            join = BasicStroke.JOIN_BEVEL;
            break;
        case MITER :
            join = BasicStroke.JOIN_MITER;
            break;
        case ROUND :
            join = BasicStroke.JOIN_ROUND;
            break;
        }
        
        //parameters for the stroke
        ctx.setColor(style.color().toAWTColor());
        BasicStroke stroke = new BasicStroke(style.width(),
                cap, join, 10f, style.dashingPattern(), 0f);
        
        //creating the polygon
        ctx.draw(stroke.createStrokedShape(pathPolyLine(polyLine)));
    }

    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        ctx.setColor(color.toAWTColor());
        //creating the shell
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
            }
            path.lineTo(p.x(), p.y());
        }
        if(polyLine.isClosed()) path.closePath();
        return path;
    }

}
