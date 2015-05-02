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

public class HGTDigitalElevationModel implements DigitalElevationModel {
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
        double lat = Math.toRadians((m.group("latOrien").equals("N") ? 1d : -1d)
                * Integer.parseInt(m.group("latCoor")));
        double lon = Math.toRadians((m.group("lonOrien").equals("E") ? 1d : -1d)
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

    @Override
    public void close() throws Exception {
        stream.close();
        buffer = null;
    }

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

        double halfS = s/2d; // Cache it this time around (whether this
                                // optimizes the performance is dubious at best,
                                // but points were taken off last time).
        double n1 = halfS * (z[0][0] - z[1][0] + z[0][1] - z[1][1]);
        double n2 = halfS * (z[0][0] + z[1][0] - z[0][1] - z[1][1]);
        double n3 = s * s;
        
        // For debugging purposes, we can print the points around p:
        /*System.out.println("(i, j): " + z[0][0]);
        System.out.println("(i, j+1): " + z[0][1]);
        System.out.println("(i+1, j): " + z[1][0]);
        System.out.println("(i+1, j+1): " + z[1][1]);*/

        return new Vector3(n1, n2, n3);
    }

    private boolean contains(PointGeo p) {
        return     p.latitude()  >= bl.latitude()
                && p.longitude() >= bl.longitude()
                && p.latitude()  <= tr.latitude()
                && p.longitude() <= tr.longitude();
    }

}
