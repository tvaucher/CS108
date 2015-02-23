package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

public class AttributedTest {
    private final Integer T = 10;

    @Test
    public void returnsCorrectAttributes() {
        Attributes.Builder attributes1 = new Attributes.Builder();
        attributes1.put("number", "1");
        attributes1.put("string", "test");
        attributes1.put("hex", "FF");
        Attributes att1 = attributes1.build();
        Attributed<Integer> attributed = new Attributed<>(T, att1);
        assertSame(att1, attributed.attributes());
    }

    @Test
    public void verificationOnType() {
        Attributed<Integer> att2 = new Attributed<>(T, new Attributes(Collections.emptyMap()));
        assertEquals(T.getClass(), att2.value().getClass()); // returns the same type has given
        assertEquals(T, att2.value()); // returns same value
    }

    @Test
    public void containsReturnsCorrectly() {
        Attributes.Builder attributes1 = new Attributes.Builder();
        attributes1.put("Contained", "true");
        Attributes att1 = attributes1.build();
        Attributed<Integer> attributed = new Attributed<>(T, att1);
        assertTrue(attributed.hasAttribute("Contained"));
        assertFalse(attributed.hasAttribute("notContained"));
    }

    @Test
    public void attributeValueWorksInAllCases() {
        Attributes.Builder attributes1 = new Attributes.Builder();
        attributes1.put("number", "1");
        attributes1.put("string", "test");
        attributes1.put("hex", "FF");
        Attributed<String> inst1 = new Attributed<>("10", attributes1.build());
        Attributed<String> inst2 = new Attributed<>("", new Attributes.Builder().build());

        // Filled list
        // In the list:
        assertEquals(inst1.attributeValue("number"), "1");
        assertEquals(inst1.attributeValue("number", "0"), "1");
        assertEquals(inst1.attributeValue("number", 0), 1);
        assertEquals(inst1.attributeValue("hex", 0), 0);

        // Not in the list:
        assertEquals(inst1.attributeValue("notinlist"), null);
        assertEquals(inst1.attributeValue("notinlist", "0"), "0");
        assertEquals(inst1.attributeValue("notinlist", 0), 0);
        assertEquals(inst1.attributeValue(""), null);
        assertEquals(inst1.attributeValue("", 0), 0);
        assertEquals(inst1.attributeValue("", "0"), "0");

        // Empty list
        assertEquals(inst2.attributeValue("notinlist"), null);
        assertEquals(inst2.attributeValue("notinlist", 0), 0);
        assertEquals(inst2.attributeValue("notinlist", "0"), "0");
        assertEquals(inst2.attributeValue(""), null);
        assertEquals(inst2.attributeValue("", 0), 0);
        assertEquals(inst2.attributeValue("", "0"), "0");
    }
}
