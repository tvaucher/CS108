package ch.epfl.imhof.utility;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public class QueryGenerator {
    private final String minLat;
    private final String maxLat;
    private final String minLon;
    private final String maxLon;
    private PointGeo bl;
    private PointGeo tr;

    private static final Projection proj = new CH1903Projection();
    private static final DecimalFormat format = new DecimalFormat("0.0000");
    private static final DecimalFormat formatLat = new DecimalFormat("00");
    private static final DecimalFormat formatLon = new DecimalFormat("000");
    private static final Pattern CH1903Pattern = Pattern
            .compile("^([4-8]\\d{5}) (\\d{5,6}) ([4-8]\\d{5}) (\\d{5,6})$");
    private static final Pattern WGS83DecimalPattern = Pattern
            .compile("^(4[5-7]\\.\\d{4}) ([6-9]\\.\\d{4}) (4[5-7]\\.\\d{4}) ([6-9]\\.\\d{4})$");

    // private static final Pattern WGS83DegreePattern =
    // Pattern.compile("^4[5-7]°[0-5]?\\d'[0-5]?\\d\" N [6-9]°[0-5]?\\d'[0-5]?\\d\" E 4[5-7]°[0-5]?\\d'[0-5]?\\d\" N [6-9]°[0-5]?\\d'[0-5]?\\d\" E$");

    public QueryGenerator(String input) {
        Matcher m = WGS83DecimalPattern.matcher(input);
        if (m.matches()) {
            minLat = m.group(1);
            maxLat = m.group(3);
            minLon = m.group(2);
            maxLon = m.group(4);
            bl = new PointGeo(Math.toRadians(Double.parseDouble(minLon)),
                    Math.toRadians(Double.parseDouble(minLat)));
            tr = new PointGeo(Math.toRadians(Double.parseDouble(maxLon)),
                    Math.toRadians(Double.parseDouble(maxLat)));

        } else {
            m = CH1903Pattern.matcher(input);
            if (m.matches()) {

                int minLatInt = Integer.parseInt(m.group(1));
                int maxLatInt = Integer.parseInt(m.group(3));
                int minLonInt = Integer.parseInt(m.group(2));
                int maxLonInt = Integer.parseInt(m.group(4));

                bl = proj.inverse(new Point(minLonInt, minLatInt));
                tr = proj.inverse(new Point(maxLonInt, maxLatInt));
                minLat = format.format(Math.toDegrees(bl.latitude()));
                maxLat = format.format(Math.toDegrees(tr.latitude()));
                minLon = format.format(Math.toDegrees(bl.longitude()));
                maxLon = format.format(Math.toDegrees(tr.longitude()));
            } else {
                throw new IllegalArgumentException(
                        "Coordinates are in an incorrect format");
            }
        }
        if (tr.latitude() < bl.latitude())
            throw new IllegalArgumentException(
                    "Maximum latitude is smaller than minimum latitude");
        if (tr.longitude() < bl.longitude())
            throw new IllegalArgumentException(
                    "Maximum longitude is smaller than minimum longitude");

    }

    public Point bl() {
        return proj.project(bl);
    }

    public Point tr() {
        return proj.project(tr);
    }

    public double getDiff() {
        return Math.toRadians(Double.parseDouble(maxLat)) - Math.toRadians(Double.parseDouble(minLat));
    }

    public String getURLosm() {
        String url = "http://overpass-api.de/api/interpreter?data=(node("
                + minLat + "," + minLon + "," + maxLat + "," + maxLon
                + ");<;>;);out;";
        System.out.println(url);
        return url;
    }

    public String getURLhgt() {
        String url = "http://www.viewfinderpanoramas.org/dem1/N"
                + formatLat.format(Math.floor(Math.toDegrees(bl.latitude())))
                + "E"
                + formatLon.format(Math.floor(Math.toDegrees(bl.longitude())))
                + ".zip";
        System.out.println(url);
        return url;
    }
}
