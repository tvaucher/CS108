package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMEntity.Builder;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class OSMNodeTest extends OSMEntityTest {

    private static final PointGeo TEST_POINT_GEO = new PointGeo(0.125621, 0.803253);

    @Override
    OSMEntity newEntity(long id, Attributes entityAttributes) {
        PointGeo testPointGeo = TEST_POINT_GEO;
        return new OSMNode(id, testPointGeo, entityAttributes);
    }

    @Override
    Builder newEntityBuilder() {
        PointGeo testPointGeo = TEST_POINT_GEO;
        return new OSMNode.Builder(1, testPointGeo);
    }

    @Test
    public void constructorAndPosition() {
        OSMNode testNode = new OSMNode(1, TEST_POINT_GEO, EMPTY_ATTRIBUTES);
        assertSame(testNode.position(), TEST_POINT_GEO);
    }

    @Test
    public void builderBuiltPosition() {
        OSMNode.Builder testBuild = new OSMNode.Builder(1, TEST_POINT_GEO);
        OSMNode testBuildResult = testBuild.build();
        assertSame(testBuildResult.position(), TEST_POINT_GEO);
    }
}
