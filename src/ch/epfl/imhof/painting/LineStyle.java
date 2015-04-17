package ch.epfl.imhof.painting;

public final class LineStyle {
    private final float width;
    private final Color color;
    private final LineCap lineCap;
    private final LineJoin lineJoin;
    private final float[] dashingPattern;
    private final static float[] EMPTY = {};
    
    public LineStyle(float width, Color color, LineCap lineCap, LineJoin lineJoin, float[] dashingPattern) {
        if (width < 0) throw new IllegalArgumentException("Line width must be >= 0, argument was " + width);
        for (int i = 0; i < dashingPattern.length; ++i) {
            if (dashingPattern[i] <= 0)
                throw new IllegalArgumentException("Dashing Pattern must only contain stricly positive elements");
        }
        
        this.width = width;
        this.color = color;
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;
        this.dashingPattern = dashingPattern.clone(); //works fine because primitive type
    }
    
    public LineStyle(float width, Color color) {
        this(width, color, LineCap.BUTT, LineJoin.MITER, EMPTY);
    }
    
    //GETTERS
    public float width() { return width; }
    public Color color() { return color; }
    public LineCap lineCap() { return lineCap; }
    public LineJoin lineJoin() { return lineJoin; }
    public float[] dashingPattern() {
        return dashingPattern.clone(); //works fine because primitive type
    }
    
    //MODIFIERS
    public LineStyle withWidth(float width) {
        //if width is neg, error'll be thrown in constructor
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }
    
    public LineStyle withColor(Color color) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }
    
    public LineStyle withLineCap(LineCap lineCap) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }
    
    public LineStyle withLineJoin(LineJoin lineJoin) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }
    
    public LineStyle withDashingPattern(float[] dashingPattern) {
        //error && copy of dashing pattern is done in constructor
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }
    
    public enum LineCap {
        BUTT, ROUND, SQUARE
    }
    
    public enum LineJoin {
        BEVEL, MITER, ROUND
    }
}
