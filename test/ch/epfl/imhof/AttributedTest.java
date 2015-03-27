package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;

public class AttributedTest {

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
}
