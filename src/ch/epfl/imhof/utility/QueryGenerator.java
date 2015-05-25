package ch.epfl.imhof.utility;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;

/**
 * Class that serves as a basis for the automatised version of the code
 * Generates the coordonates of bl an tr points. Generates the query for both
 * Overpass API and Hgt file
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 */
public final class QueryGenerator {
    private final String minLat;
    private final String maxLat;
    private final String minLon;
    private final String maxLon;
    private final Projection proj;
    private final PointGeo bl;
    private final PointGeo tr;

    private static final DecimalFormat format = new DecimalFormat("0.0000");
    private static final DecimalFormat formatLat = new DecimalFormat("00");
    private static final DecimalFormat formatLon = new DecimalFormat("000");
    private static final Pattern CH1903Pattern = Pattern
            .compile("^ *([4-8]\\d{5}) +(\\d{5,6}) +([4-8]\\d{5}) +(\\d{5,6}) *$");
    private static final Pattern WGS83DecimalPattern = Pattern
            .compile("^ *(4[5-7]\\.\\d{4}) +([6-9]\\.\\d{4}) +(4[5-7]\\.\\d{4}) +([6-9]\\.\\d{4}) *$");

    /**
     * Constructor, uses Regex to determine whether parameters are given in
     * CH1903 or WGS83
     * 
     * @param input
     *            the four coordinates separate by a space
     * @param proj
     *            the projection used for the map
     */
    public QueryGenerator(String input, Projection proj) {
        Matcher m = WGS83DecimalPattern.matcher(input);
        this.proj = proj;
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

                bl = proj.inverse(new Point(minLatInt, minLonInt));
                tr = proj.inverse(new Point(maxLatInt, maxLonInt));
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

    /**
     * return the bottom left point of the map in the class projection
     * 
     * @return bl projected
     */
    public Point bl() {
        return proj.project(bl);
    }

    /**
     * return the top right point of the map in the class projection
     * 
     * @return tr projected
     */
    public Point tr() {
        return proj.project(tr);
    }

    /**
     * return the difference of latitude in rad
     * 
     * @return difference
     */
    public double getDiff() {
        return Math.toRadians(Double.parseDouble(maxLat))
                - Math.toRadians(Double.parseDouble(minLat));
    }

    /**
     * return the query for the Overpass API
     * 
     * @return constructed query
     */
    public String getURLosm() {
        return "http://overpass-api.de/api/interpreter?data=(node(" + minLat
                + "," + minLon + "," + maxLat + "," + maxLon + ");<;>;);out;";
    }

    /**
     * return the query to get the Hgt file
     * 
     * @return address of the Hgt file
     */
    public String getURLhgt() {
        return "http://www.viewfinderpanoramas.org/dem1/N"
                + formatLat.format(Math.floor(Math.toDegrees(bl.latitude())))
                + "E"
                + formatLon.format(Math.floor(Math.toDegrees(bl.longitude())))
                + ".zip";
    }
}
