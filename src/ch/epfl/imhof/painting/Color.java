package ch.epfl.imhof.painting;

/**
 * A class that represents a color, which is very useful for the graphical
 * output. A color is described by 3 components, namely Red, Green and Blue, on
 * a scale from 0 to 1.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class Color {
    private final double r, g, b;
    private final static double BASE = 255.9999d;
    private final static int BYTE = 256;

    public final static Color RED = new Color(1, 0, 0);
    public final static Color GREEN = new Color(0, 1, 0);
    public final static Color BLUE = new Color(0, 0, 1);
    public final static Color BLACK = new Color(0, 0, 0);
    public final static Color WHITE = new Color(1, 1, 1);

    /**
     * Constructs a new Color object. This is a private method, so it can only
     * be constructed by the static classes below.
     * 
     * @param r
     *            Red value of the color, on a scale from 0 to 1.
     * @param g
     *            Green value of the color, on a scale from 0 to 1.
     * @param b
     *            Blue value of the color, on a scale from 0 to 1.
     */
    private Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Returns a newly constructed gray Color object; its "grayness" is on a
     * scale from 0 to 1, where 0 is white and 1 is black.
     * <p>
     * Throws an IllegalArgumentException if the given color is out of the
     * permitted range ([0, 1])
     * 
     * @param gray
     *            The shade of gray on a scale from 0 to 1, where 0 is white and
     *            1 is black.
     * @return A newly constructed gray Color.
     */
    public static Color gray(double gray) {
        if (!(0.0 <= gray && gray <= 1.0))
            throw new IllegalArgumentException("invalid gray component: "
                    + gray);
        return new Color(gray, gray, gray);
    }

    /**
     * Returns a newly constructed Color object of the given color.
     * <p>
     * Throws an IllegalArgumentException if the given colors are out of the
     * permitted range ([0, 1])
     * 
     * @param r
     *            The Red value, on a scale from 0 to 1.
     * @param g
     *            The Green value, on a scale from 0 to 1.
     * @param b
     *            The Blue value, on a scale from 0 to 1.
     * @return A newly constructed Color with the given parameters
     */
    public static Color rgb(double r, double g, double b) {
        if (!(0.0 <= r && r <= 1.0))
            throw new IllegalArgumentException("invalid red component: " + r);
        if (!(0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("invalid green component: " + g);
        if (!(0.0 <= b && b <= 1.0))
            throw new IllegalArgumentException("invalid blue component: " + b);
        return new Color(r, g, b);
    }

    /**
     * Returns a newly constructed Color object of the given color.
     * 
     * @param packedRGB
     *            An integer containing the information for all three colors.
     *            Red is in bits 23 to 16, Green in bits 15 to 8, and Blue in
     *            bits 7 to 0.
     * @return A newly constructed Color object of the given color.
     */
    public static Color rgb(int packedRGB) {
        double r = ((packedRGB >> 16) & 0xFF) / 255d;
        double g = ((packedRGB >> 8) & 0xFF) / 255d;
        double b = ((packedRGB >> 0) & 0xFF) / 255d;
        return rgb(r, g, b);
    }

    /**
     * A getter for the Red value of the Color.
     * 
     * @return The Red value of the Color
     */
    public double r() {
        return r;
    }

    /**
     * A getter for the Green value of the Color.
     * 
     * @return The Red value of the Color
     */
    public double g() {
        return g;
    }

    /**
     * A getter for the Blue value of the Color.
     * 
     * @return The Red value of the Color
     */
    public double b() {
        return b;
    }

    /**
     * Multiplies one color with another (by multiplying every individual
     * component of both Colors to achieve a new Color).
     * 
     * @param that
     *            Another Color.
     * @return The result of the multiplication: a new Color.
     */
    public Color multiplyWith(Color that) {
        return new Color(r * that.r, g * that.g, b * that.b);
    }

    /**
     * Converts a Color to a AWTColor (from the Java API)
     * 
     * @return
     */
    public java.awt.Color toAWTColor() {
        return new java.awt.Color((float) r, (float) g, (float) b);
    }

    /**
     * a getter for the packed RGB color
     * 
     * @return packed rgb color
     */
    public int packedRBG() {
        int red = (int) (r * BASE);
        int green = (int) (g * BASE);
        int blue = (int) (b * BASE);
        return red * BYTE * BYTE + green * BYTE + blue;
    }
}
