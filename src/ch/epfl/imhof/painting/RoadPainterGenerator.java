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
        Predicate<Attributed<?>> bridgePredicate = Filters.tagged("bridge"), tunnelPredicate = Filters
                .tagged("tunnel"), roadPredicate = bridgePredicate.or(
                tunnelPredicate).negate(); // De Morgan's law
        for (RoadSpec spec : specs) {
            // Définition des styles
            float[] tunnelDashingStyle = { 2 * spec.w_i(), 2 * spec.w_i() };
            LineStyle bridgeInsideStyle = new LineStyle(spec.w_i(), spec.c_i(),
                    LineCap.ROUND, LineJoin.ROUND, LineStyle.EMPTY), bridgeBorderStyle = new LineStyle(
                    spec.w_i() + 2 * spec.w_c(), spec.c_c(), LineCap.BUTT,
                    LineJoin.ROUND, LineStyle.EMPTY), roadInsideStyle = bridgeInsideStyle, roadBorderStyle = bridgeBorderStyle
                    .withLineCap(LineCap.ROUND), tunnelStyle = new LineStyle(
                    spec.w_i() / 2, spec.c_c(), LineCap.BUTT, LineJoin.ROUND,
                    tunnelDashingStyle);

            // Définition des peintres
            bridgeInside = bridgeInside.above(line(bridgeInsideStyle).when(
                    spec.roadType().and(bridgePredicate)));
            bridgeBorder = bridgeBorder.above(line(bridgeBorderStyle).when(
                    spec.roadType().and(bridgePredicate)));
            roadInside = roadInside.above(line(roadInsideStyle).when(
                    spec.roadType().and(roadPredicate)));
            roadBorder = roadBorder.above(line(roadBorderStyle).when(
                    spec.roadType().and(roadPredicate)));
            tunnel = tunnel.above(line(tunnelStyle).when(
                    spec.roadType().and(tunnelPredicate)));
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
        private final float w_i, w_c;
        private final Color c_i, c_c;

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
            this.w_i = w_i;
            this.c_i = c_i;
            this.w_c = w_c;
            this.c_c = c_c;
        }

        /**
         * getter for the predicate
         * 
         * @return roadType
         */
        public Predicate<Attributed<?>> roadType() {
            return roadType;
        }

        /**
         * getter for width 1
         * 
         * @return w_i
         */
        public float w_i() {
            return w_i;
        }

        /**
         * getter for width 2
         * 
         * @return w_c
         */
        public float w_c() {
            return w_c;
        }

        /**
         * getter for color 1
         * 
         * @return c_i
         */
        public Color c_i() {
            return c_i;
        }

        /**
         * getter for color 2
         * 
         * @return c_c
         */
        public Color c_c() {
            return c_c;
        }
    }
}
