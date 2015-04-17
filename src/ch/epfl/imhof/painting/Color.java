package ch.epfl.imhof.painting;

public final class Color {
    private final double r, g, b;

    public final static Color RED = new Color(1, 0, 0);
    public final static Color GREEN = new Color(0, 1, 0);
    public final static Color BLUE = new Color(0, 0, 1);
    public final static Color BLACK = new Color(0, 0, 0);
    public final static Color WHITE = new Color(1, 1, 1);

    private Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public static Color gray(double gray) {
        if (! (0.0 <= gray && gray <= 1.0))
            throw new IllegalArgumentException("invalid gray component: " + gray);
        return new Color(gray, gray, gray);
    }
    
    public static Color rgb(double r, double g, double b) {
        if (! (0.0 <= r && r <= 1.0))
            throw new IllegalArgumentException("invalid red component: " + r);
        if (! (0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("invalid green component: " + g);
        if (! (0.0 <= b && b <= 1.0))
            throw new IllegalArgumentException("invalid blue component: " + b);
        return new Color(r, g, b);
    }

    public static Color rgb(int packedRGB) {
        double r = ((packedRGB >> 16) & 0xFF) / 255d;
        double g = ((packedRGB >>  8) & 0xFF) / 255d;
        double b = ((packedRGB >>  0) & 0xFF) / 255d;
        return rgb(r, g, b);          
    }

    public double r() { return r; }
    public double g() { return g; }
    public double b() { return b; }

    public Color multiplyWith(Color that) {
        return new Color(r*that.r, g*that.g, b*that.b);
    }

    public java.awt.Color toAWTColor() {
        return new java.awt.Color((float) r, (float) g, (float) b);
    }
}
