package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * HGTDigitalElevationModel is a class that can read and interpret Digital
 * Elevation Models (DEM) in the HGT format, and most importantly, calculate the
 * altitude of a given point based on the HGT data (for more information on HGT
 * files, see {@link http://www.viewfinderpanoramas.org/dem3.html} )
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {
    private final PointGeo bl, tr;
    private final double delta; // [rad]
    private final double s; // the distance a dot and the next, in [m]
    private final FileInputStream stream;
    private ShortBuffer buffer;
    private final int side; // The number of points on one axis in the
                            // altitude plot (usually 3601)

    private final static Pattern hgtPattern = Pattern
            .compile("^(?<latOrien>[NS])(?<latCoor>\\d{2})(?<lonOrien>[EW])(?<lonCoor>\\d{3}).hgt$");
    private final static double oneDegToRad = Math.PI / 180;

    /**
     * Constructs a new HGTDigitalElevationModel from a given .hgt file.
     * <p>
     * Technically speaking, it loads the file into a buffer (which is the most
     * efficient way of reading it), performs a few initial calculations for the
     * DEM's position and size, among others.
     * 
     * @param hgt
     *            A .hgt file
     * @throws IOException
     *             Should an exception be thrown by the process of reading the
     *             file, this method will just forward it.
     * @throws IllegalArgumentException
     *             If the filename is invalid (it should follow the strict
     *             naming conditions described under the HGT File Format section
     *             at {@link http ://www.viewfinderpanoramas.org/dem3.html}
     * @throws IllegalArgumentException
     *             If the DEM isn't a square.
     */
    public HGTDigitalElevationModel(File hgt) throws IOException {
        // Uses regex to test and get parts of the args in the file name
        Matcher m = hgtPattern.matcher(hgt.getName());
        if (!m.matches())
            throw new IllegalArgumentException("Invalid file name");

        // Test about the length

        long length = hgt.length();
        double dSide = Math.sqrt(length / 2);
        side = (int) dSide;
        if (side != dSide)
            throw new IllegalArgumentException(
                    "File must contain a sqrt of length must be even and an integer");

        // Creation of bottom left point
        double lat = Math
                .toRadians((m.group("latOrien").equals("N") ? 1d : -1d)
                        * Integer.parseInt(m.group("latCoor")));
        double lon = Math
                .toRadians((m.group("lonOrien").equals("E") ? 1d : -1d)
                        * Integer.parseInt(m.group("lonCoor")));
        bl = new PointGeo(lon, lat);
        tr = new PointGeo(lon + oneDegToRad, lat + oneDegToRad);

        // Computation of resolution
        delta = oneDegToRad / side;
        s = Earth.RADIUS * delta;
        // 1 deg (in rad) contains side points (2 bytes/point)

        // Creation of buffer for reading
        stream = new FileInputStream(hgt);
        buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, length)
                .asShortBuffer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        stream.close();
        buffer = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.dem.DigitalElevationModel#normalAt(ch.epfl.imhof.PointGeo)
     */
    @Override
    public Vector3 normalAt(PointGeo p) throws IllegalArgumentException {
        if (!contains(p))
            throw new IllegalArgumentException("Your point is in another file");
        // Mario ref, AKA Your princess is in another castle

        // Calculating the grid number that we have to get data from.
        int i = (int) Math.floor((p.longitude() - bl.longitude()) / delta);
        int j = (int) Math.floor((tr.latitude() - p.latitude()) / delta);

        // z will be our table of heights for the 4 points around p.
        short[][] z = new short[2][2];
        // Fetching heights of the 4 points around p
        z[0][0] = buffer.get(i + side * (j + 1));
        z[0][1] = buffer.get(i + side * j);
        z[1][0] = buffer.get((i + 1) + side * (j + 1));
        z[1][1] = buffer.get((i + 1) + side * j);

        double halfS = s / 2d; // Cache it this time around (whether this
                               // optimizes the performance is dubious at best,
                               // but points were taken off last time).
        double n1 = halfS * (z[0][0] - z[1][0] + z[0][1] - z[1][1]);
        double n2 = halfS * (z[0][0] + z[1][0] - z[0][1] - z[1][1]);
        double n3 = s * s;

        return new Vector3(n1, n2, n3).normalized();
    }

    /**
     * Returns whether the DEM contains a certain point p.
     * 
     * @param p
     *            The PointGeo object that will be checked.
     * @return Whether the given PointGeo object's coordinates are within the
     *         DEM.
     */
    public boolean contains(PointGeo p) {
        return p.latitude() >= bl.latitude() && p.longitude() >= bl.longitude()
                && p.latitude() <= tr.latitude()
                && p.longitude() <= tr.longitude();
    }

}
