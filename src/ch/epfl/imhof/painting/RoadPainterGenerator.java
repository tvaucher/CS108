package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import static ch.epfl.imhof.painting.Painter.empty;
import static ch.epfl.imhof.painting.Painter.line;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Non-instantiable class that generates a painter for the roads. It consists of
 * stacking multiple dedicated painters that draw each type of road described in
 * the RoadSpec array, in a 5-layered way.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 */
public final class RoadPainterGenerator {
    private RoadPainterGenerator() {
    }

    /**
     * Construct a generalized painter that draws the road network. Consists of
     * 5 specialized painters (bridge inside, bridge border, road inside, road
     * border, tunnel).
     * 
     * Those painters are stacked on top of themselves for each given road spec
     * and finally, they are stacked above one another in the order given
     * previously.
     * 
     * @param specs
     *            array of specificities that contains the details about the
     *            roads to draw
     * @return a painter for the road network
     */
    public static Painter painterForRoads(RoadSpec... specs) {
        Painter bridgeInside = empty(), bridgeBorder = empty(), roadInside = empty(), roadBorder = empty(), tunnel = empty();

        for (RoadSpec spec : specs) {
            // Définition des peintres
            bridgeInside = bridgeInside.above(spec.brideInside());
            bridgeBorder = bridgeBorder.above(spec.brideBorder());
            roadInside = roadInside.above(spec.roadInside());
            roadBorder = roadBorder.above(spec.roadBorder());
            tunnel = tunnel.above(spec.tunnel());
        }
        return bridgeInside.above(bridgeBorder).above(roadInside)
                .above(roadBorder).above(tunnel);
    }

    /**
     * Utility class that describes the specificities of a road.
     * 
     * @author Maxime Kjaer (250694)
     * @author Timote Vaucher (246532)
     *
     */
    public static class RoadSpec {
        private final Predicate<Attributed<?>> roadType;

        private final static Predicate<Attributed<?>> bridgePredicate = Filters
                .tagged("bridge");
        private final static Predicate<Attributed<?>> tunnelPredicate = Filters
                .tagged("tunnel");
        private final static Predicate<Attributed<?>> roadPredicate = bridgePredicate
                .or(tunnelPredicate).negate(); // De Morgan's law

        private final LineStyle bridgeInsideStyle;
        private final LineStyle bridgeBorderStyle;
        private final LineStyle roadInsideStyle;
        private final LineStyle roadBorderStyle;
        private final LineStyle tunnelStyle;

        /**
         * Construct a RoadSpec object
         * 
         * @param roadType
         *            predicate of which type of road will be drawn
         * @param w_i
         *            width 1
         * @param c_i
         *            color 1
         * @param w_c
         *            width 2
         * @param c_c
         *            color 2
         */
        public RoadSpec(Predicate<Attributed<?>> roadType, float w_i,
                Color c_i, float w_c, Color c_c) {
            this.roadType = roadType;

            float[] tunnelDashingStyle = { 2 * w_i, 2 * w_i };
            bridgeInsideStyle = new LineStyle(w_i, c_i, LineCap.ROUND,
                    LineJoin.ROUND, LineStyle.EMPTY);
            bridgeBorderStyle = new LineStyle(w_i + 2 * w_c, c_c, LineCap.BUTT,
                    LineJoin.ROUND, LineStyle.EMPTY);
            roadInsideStyle = bridgeInsideStyle;
            roadBorderStyle = bridgeBorderStyle.withLineCap(LineCap.ROUND);
            tunnelStyle = new LineStyle(w_i / 2, c_c, LineCap.BUTT,
                    LineJoin.ROUND, tunnelDashingStyle);
        }

        /**
         * painter for the inside of bridge for a certain spec
         * 
         * @return bridge inside painter
         */
        public Painter brideInside() {
            return line(bridgeInsideStyle).when(roadType.and(bridgePredicate));
        }

        /**
         * painter for the border of bridge for a certain spec
         * 
         * @return bridge border painter
         */
        public Painter brideBorder() {
            return line(bridgeBorderStyle).when(roadType.and(bridgePredicate));
        }

        /**
         * painter for the inside of roads for a certain spec
         * 
         * @return road inside painter
         */
        public Painter roadInside() {
            return line(roadInsideStyle).when(roadType.and(roadPredicate));
        }

        /**
         * painter for the border of roads for a certain spec
         * 
         * @return road border painter
         */
        public Painter roadBorder() {
            return line(roadBorderStyle).when(roadType.and(roadPredicate));
        }

        /**
         * painter for the tunnels for a certain spec
         * 
         * @return tunnel painter
         */
        public Painter tunnel() {
            return line(tunnelStyle).when(roadType.and(tunnelPredicate));
        }

    }
}
