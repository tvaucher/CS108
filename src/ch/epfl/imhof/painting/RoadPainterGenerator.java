package ch.epfl.imhof.painting;


import java.util.function.Predicate;

import static ch.epfl.imhof.painting.Painter.empty;
import static ch.epfl.imhof.painting.Painter.line;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;


public final class RoadPainterGenerator {
    private RoadPainterGenerator() {}
    
    public static Painter painterForRoads(RoadSpec... specs) {
        Painter bridgeInside = empty(), bridgeBorder = empty(), roadInside = empty(), roadBorder = empty(), tunnel = empty();
        Predicate<Attributed<?>> bridgePredicate = Filters.tagged("bridge"),
                tunnelPredicate = Filters.tagged("tunnel"),
                roadPredicate = bridgePredicate.negate().and(tunnelPredicate.negate());
        for (RoadSpec spec : specs) {
            //Définition des styles
            float[] tunnelDashingStyle = {2*spec.w_i(), 2*spec.w_i()};
            LineStyle bridgeInsideStyle = new LineStyle(spec.w_i(), spec.c_i(), LineCap.ROUND, LineJoin.ROUND, LineStyle.EMPTY),
            bridgeBorderStyle = new LineStyle(spec.w_i()+2*spec.w_c(), spec.c_c(), LineCap.BUTT, LineJoin.ROUND, LineStyle.EMPTY),
            roadInsideStyle = bridgeInsideStyle,
            roadBorderStyle = bridgeBorderStyle.withLineCap(LineCap.ROUND),
            tunnelStyle = new LineStyle(spec.w_i()/2, spec.c_c(), LineCap.BUTT, LineJoin.ROUND, tunnelDashingStyle);
            
            //Définition des peintres
            /*bridgeInside = line(bridgeInsideStyle).when(spec.roadType().and(bridgePredicate)).above(bridgeInside);
            bridgeBorder = line(bridgeBorderStyle).when(spec.roadType().and(bridgePredicate)).above(bridgeBorder);
            roadInside = line(roadInsideStyle).when(spec.roadType().and(roadPredicate)).above(roadInside);
            roadBorder = line(roadBorderStyle).when(spec.roadType().and(roadPredicate)).above(roadBorder);
            tunnel = line(tunnelStyle).when(spec.roadType().and(tunnelPredicate)).above(tunnel);*/
            bridgeInside = bridgeInside.above(line(bridgeInsideStyle).when(spec.roadType().and(bridgePredicate)));
            bridgeBorder = bridgeBorder.above(line(bridgeBorderStyle).when(spec.roadType().and(bridgePredicate)));
            roadInside = roadInside.above(line(roadInsideStyle).when(spec.roadType().and(roadPredicate)));
            roadBorder = roadBorder.above(line(roadBorderStyle).when(spec.roadType().and(roadPredicate)));
            tunnel = tunnel.above(line(tunnelStyle).when(spec.roadType().and(tunnelPredicate)));
        }
        return bridgeInside.above(bridgeBorder).above(roadInside).above(roadBorder).above(tunnel);
    }
    
    public static class RoadSpec {
        private final Predicate<Attributed<?>> roadType;
        private final float w_i, w_c;
        private final Color c_i, c_c;
        
        public RoadSpec(Predicate<Attributed<?>> roadType, float w_i, Color c_i, float w_c, Color c_c) {
            this.roadType = roadType;
            this.w_i = w_i;
            this.c_i = c_i;
            this.w_c = w_c;
            this.c_c = c_c;
        }

        public Predicate<Attributed<?>> roadType() {
            return roadType;
        }

        public float w_i() {
            return w_i;
        }

        public float w_c() {
            return w_c;
        }

        public Color c_i() {
            return c_i;
        }

        public Color c_c() {
            return c_c;
        }
    }
}
