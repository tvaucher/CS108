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
    private final double sigma; // [rad]
    private final FileInputStream stream;
    private final ShortBuffer buffer;

    private final static Pattern hgtPattern = Pattern
            .compile("^([NS])(\\d{2})([EW])(\\d{3}).hgt$");
    private final static double oneDegToRad = Math.PI / 180;

    public HGTDigitalElevationModel(File hgt) throws IOException {
        // Uses regex to test and get parts of the args in the file name
        Matcher m = hgtPattern.matcher(hgt.getName());
        if (!m.matches())
            throw new IllegalArgumentException("Invalid file name");
        
        //Test about the length
        long length = hgt.length();
        double side = Math.sqrt(length);
        if (side % 2 != 0)
            throw new IllegalArgumentException(
                    "File must contain a sqrt of length must be even and an integer");

        // Creation of bottom left point
        double lat = Math.toRadians((m.group(1).equals("N") ? 1d : -1d)
                * Integer.parseInt(m.group(2)));
        double lon = Math.toRadians((m.group(3).equals("E") ? 1d : -1d)
                * Integer.parseInt(m.group(4)));
        bl = new PointGeo(lon, lat);
        tr = new PointGeo(lon + oneDegToRad, lat + oneDegToRad);

        // Computation of resolution
        sigma = oneDegToRad / (side / 2);
        // 1 deg (in rad) contains side points (2 bytes/point)

        // Creation of buffer for reading
        stream = new FileInputStream(hgt);
        buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, length)
                .asShortBuffer();
    }

    @Override
    public void close() throws Exception {
        stream.close();
    }

    @Override
    public Vector3 normalAt(PointGeo p) throws IllegalArgumentException {
        if (!contains(p))
            throw new IllegalArgumentException("Your point is in another file"); //Mario ref
        //notes : sigma is already in radians
        //You can use oneDegToRad if you want a const that transforms 1 deg in rad.
        //there already is bl and tr points, if you want any other feel free to had.
        
        return null;
    }

    private boolean contains(PointGeo p) {
        return p.latitude() >= bl.latitude() && p.longitude() >= bl.longitude()
                && p.latitude() <= tr.latitude()
                && p.longitude() <= tr.longitude();
    }

}
