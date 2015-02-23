package ch.epfl.imhof.projection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public class CH1903ProjectionTest {
    private static final double DELTA = 0.000001;

    @Test
    public void projectProducesTheRightValues() {
        Projection p = new CH1903Projection();
        // Musée Olympique à Lausanne ( 6.634088, 46.508732 )
        assertEquals( 538249.192648, p.project(new PointGeo(0.115786678, 0.811730504)).x(), DELTA );
        assertEquals( 151142.504634, p.project(new PointGeo(0.115786678, 0.811730504)).y(), DELTA );
        // Fosse aux ours de Berne ( 7.4600062, 46.947973 )
        assertEquals( 601627.169584, p.project(new PointGeo(0.13020167, 0.819396706)).x(), DELTA );
        assertEquals( 199654.501720, p.project(new PointGeo(0.13020167, 0.819396706)).y(), DELTA );
        // Swissminiatur à Lugano ( 8.9431137, 45.9444981 )
        assertEquals( 716668.533051, p.project(new PointGeo(0.156086779, 0.801882765)).x(), DELTA );
        assertEquals( 89219.439903, p.project(new PointGeo(0.156086779, 0.801882765)).y(), DELTA );
        // QG de la FIFA à Zurich ( 8.5749969, 47.3816222 )
        assertEquals( 685810.833124, p.project(new PointGeo(0.149661929, 0.826965312)).x(), DELTA );
        assertEquals( 248486.441073, p.project(new PointGeo(0.149661929, 0.826965312)).y(), DELTA );
        // Cathédrale de Bâle ( 7.592443, 47.556462 )
        assertEquals( 611577.774979, p.project(new PointGeo(0.132513129, 0.830016842)).x(), DELTA );
        assertEquals( 267316.611467, p.project(new PointGeo(0.132513129, 0.830016842)).y(), DELTA );
        // Exemple du document ( 8° 43' 49.79", 46° 2' 38.87" )
        assertEquals( 700108.820134, p.project(new PointGeo(0.1524, 0.8036)).x(), DELTA );
        assertEquals( 99863.666660, p.project(new PointGeo(0.1524, 0.8036)).y(), DELTA );
    }

    @Test
    public void inverseProducesTheRightValues() {
        Projection p = new CH1903Projection();
        // Musée Olympique à Lausanne ( 6.634088, 46.508732 )
        Point projectionM = p.project(new PointGeo(0.115786678, 0.811730504));
        assertEquals( 0.115786678, p.inverse(projectionM).longitude(), DELTA );
        assertEquals( 0.811730504, p.inverse(projectionM).latitude(), DELTA );
        // Fosse aux ours de Berne ( 7.4600062, 46.947973 )
        Point projectionBB = p.project(new PointGeo(0.13020167, 0.819396706));
        assertEquals( 0.13020167, p.inverse(projectionBB).longitude(), DELTA );
        assertEquals( 0.819396706, p.inverse(projectionBB).latitude(), DELTA );
        // Swissminiatur à Lugano ( 8.9431137, 45.9444981 )
        Point projectionS = p.project(new PointGeo(0.156086779, 0.801882765));
        assertEquals( 0.156086779, p.inverse(projectionS).longitude(), DELTA );
        assertEquals( 0.801882765, p.inverse(projectionS).latitude(), DELTA );
        // QG de la FIFA à Zurich ( 8.5749969, 47.3816222 )
        Point projectionF = p.project(new PointGeo(0.149661929, 0.826965312));
        assertEquals( 0.149661929, p.inverse(projectionF).longitude(), DELTA );
        assertEquals( 0.826965312, p.inverse(projectionF).latitude(), DELTA );
        // Cathédrale de Bâle ( 7.592443, 47.556462 )
        Point projectionBM = p.project(new PointGeo(0.132513129, 0.830016842));
        assertEquals( 0.132513129, p.inverse(projectionBM).longitude(), DELTA );
        assertEquals( 0.830016842, p.inverse(projectionBM).latitude(), DELTA );
        // Exemple du document ( 8° 43' 49.79", 46° 2' 38.87" )
        Point projectionD = p.project(new PointGeo(0.1524, 0.8036));
        assertEquals( 0.1524, p.inverse(projectionD).longitude(), DELTA );
        assertEquals( 0.8036, p.inverse(projectionD).latitude(), DELTA );
    }
}
