package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;

public class AttributedTest {

    private final Integer T = 10;
    
    
	private Attributes newSampleAttributes() {
		HashMap<String, String> testData = new HashMap<>();
		testData.put("testKey 1", "testValue 1");
		testData.put("testKey 2", "testValue 2");
		testData.put("testKey 3", "testValue 3");
		testData.put("testKey 4", "23");
		return new Attributes(testData);
	}
	
	@Test
	public void constructorAndGetters() {
		Attributes testAttributes = newSampleAttributes();
		Attributed<Integer> testAttributed = new Attributed<>(5, testAttributes);
		assertTrue(testAttributed.value() == 5);
		assertSame(testAttributes, testAttributed.attributes());
	}

	@Test
	public void hasAttributeAndAttributeValueVerification() {
		Attributes testAttributes = newSampleAttributes();
		Attributed<Integer> testAttributed = new Attributed<>(5, testAttributes);
		assertTrue(testAttributed.hasAttribute("testKey 1")
				&& testAttributed.hasAttribute("testKey 2")
				&& testAttributed.hasAttribute("testKey 3")
				&& !testAttributed.hasAttribute("testKey 5"));
		assertEquals("testValue 1", testAttributed.attributeValue("testKey 1"));
		assertEquals("testValue 2", testAttributed.attributeValue("testKey 2"));
		assertEquals("testValue 3", testAttributed.attributeValue("testKey 3"));
		
        // Our own tests:
		Attributes.Builder attributes1 = new Attributes.Builder();
        attributes1.put("number", "1");
        attributes1.put("string", "test");
        attributes1.put("hex", "FF");
        Attributes att1 = attributes1.build();
        Attributed<Integer> attributed = new Attributed<>(T, att1);
        assertSame(att1, attributed.attributes());

	}

	@Test
	public void getAttributeValueWithDefaultValue() {
		Attributes testAttributes = newSampleAttributes();
		Attributed<String> testAttributed = new Attributed<>("test",
				testAttributes);
		assertEquals("testValue 1",
				testAttributed.attributeValue("testKey 1", "default"));
		assertEquals("default",
				testAttributed.attributeValue("testKey 6", "default"));
	}

	@Test
	public void getAttributeValueWithDefaultInt() {
		Attributes testAttributes = newSampleAttributes();
		Attributed<Double> testAttributed = new Attributed<>(7.45,
				testAttributes);
		assertEquals(65, testAttributed.attributeValue("testKey 1", 65));
		assertEquals(23, testAttributed.attributeValue("testKey 4", 65));
		assertEquals(65, testAttributed.attributeValue("testKey 6", 65));
	}
	
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

    
}
