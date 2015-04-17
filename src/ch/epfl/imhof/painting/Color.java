package ch.epfl.imhof.painting;

public final class Color {
    private final double r, g, b;

    public final static Color RED = new Color(1, 0, 0);
    public final static Color GREEN = new Color(0, 1, 0);
    public final static Color BLUE = new Color(0, 0, 1);
    public final static Color BLACK = new Color(0, 0, 0);
    public final static Color WHITE = new Color(1, 1, 1);

    private Color(double r, double g, double b) {
        
    }
    public static Color rgb(double r, double g, double b) {
        if (! (0.0 <= r && r <= 1.0))
            throw new IllegalArgumentException("invalid red component: " + r);
        if (! (0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("invalid green component: " + g);
        if (! (0.0 <= b && b <= 1.0))
            throw new IllegalArgumentException("invalid blue component: " + b);

        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Construit une couleur en « déballant » les trois composantes RGB stockées
     * chacune sur 8 bits. La composante R est supposée occuper les bits 23 à
     * 16, la composante G les bits 15 à 8 et la composante B les bits 7 à 0.
     * Les composantes sont de plus supposées gamma-encodées selon le standard
     * sRGB.
     *
     * @param packedRGB
     *            la couleur encodée, au format RGB.
     */
    public Color(int packedRGB) {
        this(gammaDecode(((packedRGB >> 16) & 0xFF) / 255d),
                gammaDecode(((packedRGB >>  8) & 0xFF) / 255d),
                gammaDecode(((packedRGB >>  0) & 0xFF) / 255d));
    }

    /**
     * Retourne la composante rouge de la couleur, comprise entre 0 et 1.
     *
     * @return la composante rouge de la couleur.
     */
    public double r() { return r; }

    /**
     * Retourne la composante verte de la couleur, comprise entre 0 et 1.
     *
     * @return la composante verte de la couleur.
     */
    public double g() { return g; }

    /**
     * Retourne la composante bleue de la couleur, comprise entre 0 et 1.
     *
     * @return la composante bleue de la couleur.
     */
    public double b() { return b; }

    /**
     * Mélange la couleur réceptrice avec la proportion donnée de la couleur
     * passée en argument. Si la proportion vaut 0, la couleur retournée est la
     * même que la couleur réceptrice, alors que si elle vaut 1, la couleur est
     * la même que la couleur passée en argument.
     *
     * @param that
     *            la couleur avec laquelle mélanger la couleur réceptrice.
     * @param p
     *            la proportion de la couleur passée à inclure dans le mélange,
     *            comprise entre 0 et 1.
     * @return la couleur résultant du mélange.
     * @throws IllegalArgumentException
     *             si la proportion est hors de l'intervalle [0;1].
     */
    public Color mixWith(Color that, double p) {
        if (! (0.0 <= p && p <= 1.0))
            throw new IllegalArgumentException("invalid mix proportion: " + p);
        return new Color(r + p * (that.r - r), g + p * (that.g - g), b + p * (that.b - b));
    }

    /**
     * Convertit la couleur en une couleur AWT.
     * @return La couleur AWT correspondant à la couleur réceptrice.
     */
    public java.awt.Color toAWTColor() {
        return new java.awt.Color(gammaEncode(r), gammaEncode(g), gammaEncode(b));
    }
}
