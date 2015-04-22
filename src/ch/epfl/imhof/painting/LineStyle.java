package ch.epfl.imhof.painting;

/**
 * LineStyle is a class describing how a line should look on the output,
 * including width, color, type of linecaps and linejoins, dashing patterns,
 * etc.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public class LineStyle {
    private final float width;
    private final Color color;
    private final LineCap lineCap;
    private final LineJoin lineJoin;
    private final float[] dashingPattern;
    public final static float[] EMPTY = {};

    /**
     * Constructs a new LineStyle object
     * 
     * @param width
     *            The width of the line (>= 0)
     * @param color
     *            The Color of the line
     * @param lineCap
     *            The type of linecap (butt, round or square)
     * @param lineJoin
     *            The type of linejoin (bevel, miter, round)
     * @param dashingPattern
     *            A list of the lengths of the dashes (should all be > 0)
     * 
     * @throws IllegalArgumentException
     *             if an argument is outside of its allowed bounds.
     */
    public LineStyle(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float[] dashingPattern) {
        if (width < 0)
            throw new IllegalArgumentException(
                    "Line width must be >= 0, argument was " + width);
        for (int i = 0; i < dashingPattern.length; ++i) {
            if (dashingPattern[i] <= 0)
                throw new IllegalArgumentException(
                        "Dashing Pattern must only contain stricly positive elements");
        }

        this.width = width;
        this.color = color;
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;
        this.dashingPattern = dashingPattern.clone(); // works fine because
                                                      // primitive type
    }

    /**
     * Constructs a new LineStyle object with default values for unspecified
     * arguments using the previous constructor.
     * <p>
     * Per default, the linecap will be a butt, the linejoin will be a miter,
     * and the line won't be dashed.
     * 
     * @param width
     *            The width of the line
     * @param color
     *            The Color of the line
     */
    public LineStyle(float width, Color color) {
        this(width, color, LineCap.BUTT, LineJoin.MITER, EMPTY);
    }

    // GETTERS: There is a getter for every field.

    /**
     * A getter for the line's specified width.
     * 
     * @return The specified width of the line.
     */
    public float width() {
        return width;
    }

    /**
     * A getter for the line's specified Color.
     * 
     * @return The specified Color of the line.
     */
    public Color color() {
        return color;
    }

    /**
     * A getter for the line's specified linecap.
     * 
     * @return The specified linecap of the line.
     */
    public LineCap lineCap() {
        return lineCap;
    }

    /**
     * A getter for the line's specified linejoin.
     * 
     * @return The specified linejoin of the line.
     */
    public LineJoin lineJoin() {
        return lineJoin;
    }

    /**
     * A getter for the line's specified dashing pattern ({} if the line isn't
     * dashed)
     * 
     * @return The specified dashing pattern of the line.
     */
    public float[] dashingPattern() {
        return dashingPattern.clone(); // works fine because primitive type
    }

    // MODIFIERS: It is possible to create a new instance of a similar style,
    // where only a single attribute has been changed:

    /**
     * An identical LineStyle, albeit with a new width.
     * 
     * @param width
     *            The new width of the new LineStyle
     * @return A new LineStyle, similar in every aspect but that of the width.
     */
    public LineStyle withWidth(float width) {
        // if width is neg, error'll be thrown in constructor
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * An identical LineStyle, albeit with a new Color.
     * 
     * @param color
     *            The new Color of the new LineStyle
     * @return A new LineStyle, similar in every aspect but that of the Color.
     */
    public LineStyle withColor(Color color) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * An identical LineStyle, albeit with a new linecap.
     * 
     * @param lineCap
     *            The new linecap of the new LineStyle
     * @return A new LineStyle, similar in every aspect but that of the linecap.
     */
    public LineStyle withLineCap(LineCap lineCap) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * An identical LineStyle, albeit with a new linejoin.
     * 
     * @param lineJoin
     *            The new linejoin of the new LineStyle
     * @return A new LineStyle, similar in every aspect but that of the
     *         linejoin.
     */
    public LineStyle withLineJoin(LineJoin lineJoin) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * An identical LineStyle, albeit with a new dashing pattern.
     * 
     * @param dashingPattern
     *            The new dashing pattern of the new LineStyle
     * @return A new LineStyle, similar in every aspect but that of the dashing
     *         pattern.
     */
    public LineStyle withDashingPattern(float[] dashingPattern) {
        // error && copy of dashing pattern is done in constructor
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * An enumeration of the possible line capping styles. 
     */
    public enum LineCap {
        BUTT, ROUND, SQUARE
    }

    /**
     * An enumeration of the possible line joining styles.
     */
    public enum LineJoin {
        BEVEL, MITER, ROUND
    }
}
