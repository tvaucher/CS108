package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;

public abstract class OSMEntityTest {

    protected static final Attributes EMPTY_ATTRIBUTES = new Attributes(Collections.emptyMap());

    abstract OSMEntity newEntity(long id, Attributes entityAttributes);

    abstract OSMEntity.Builder newEntityBuilder();

    @Test
    public void constructorAndID() {
        OSMEntity testEntity = newEntity(1L, EMPTY_ATTRIBUTES);
        assertEquals(testEntity.id(), 1L);
    }

    @Test
    public void constructorAndAttributes() {
        OSMEntity testEntity = newEntity(1, EMPTY_ATTRIBUTES);
        assertSame(testEntity.attributes(), EMPTY_ATTRIBUTES);
    }

    @Test
    public void constructorAndHasAttribute() {
        HashMap<String, String> testData = new HashMap<>();
        testData.put("testKey 1", "testValue 1");
        testData.put("testKey 2", "testValue 2");
        testData.put("testKey 3", "testValue 3");
        Attributes testAttributes = new Attributes(testData);
        OSMEntity testEntity = newEntity(1, testAttributes);
        assertTrue(testEntity.hasAttribute("testKey 1")
                && testEntity.hasAttribute("testKey 2")
                && testEntity.hasAttribute("testKey 3"));
    }

    @Test
    public void constructorAndAttributeValue() {
        HashMap<String, String> testData = new HashMap<>();
        testData.put("testKey 1", "testValue 1");
        testData.put("testKey 2", "testValue 2");
        testData.put("testKey 2", "testValue 3");
        Attributes testAttributes = new Attributes(testData);
        OSMEntity testEntity = newEntity(1, testAttributes);
        assertTrue(testEntity.attributeValue("testKey 1").equals("testValue 1")
                && testEntity.attributeValue("testKey 2").equals("testValue 3")
                && testEntity.attributeValue("randomKey123") == null);
    }

    @Test
    public void builderBuiltSetAttribute() throws Throwable {
        OSMEntity.Builder testBuild = newEntityBuilder();
        testBuild.setAttribute("testKey 1", "testValue 1");
        // Test if setAttribute overwrites old value
        testBuild.setAttribute("testKey 1", "testValue 3");
        testBuild.setAttribute("testKey 2", "testValue 2");
        OSMEntity entity = invokeBuild(testBuild);
        assertTrue(entity.hasAttribute("testKey 1") && entity.attributeValue("testKey 1").equals( "testValue 3")
                && entity.hasAttribute("testKey 2") && entity.attributeValue("testKey 2").equals("testValue 2"));
    }

    @Test
    public void builderAndSetIncompleteAndIsIncomplete() {
        OSMEntity.Builder testBuild = newEntityBuilder();
        testBuild.setIncomplete();
        assertTrue(testBuild.isIncomplete());
    }


    @Test(expected = IllegalStateException.class)
    public void builderBuildWhenIncompleteThrowsException() throws Throwable {
        OSMEntity.Builder builder = newEntityBuilder();
        builder.setIncomplete();
        invokeBuild(builder);
    }

    /**
     * This test has to be overridden for OSMWay
     */
    @Test
    public void builderIsIncomplete() {
        OSMEntity.Builder builder = newEntityBuilder();
        assertFalse(builder.isIncomplete());
        builder.setIncomplete();
        assertTrue(builder.isIncomplete());
    }

    /**
     * Since the method build was not defined in OSMEntity,
     * I used the reflection for this test
     */
    private OSMEntity invokeBuild(OSMEntity.Builder builder) throws Throwable {
        Method methodBuild = builder.getClass().getMethod("build");
        try {
            return (OSMEntity) methodBuild.invoke(builder);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }


}
