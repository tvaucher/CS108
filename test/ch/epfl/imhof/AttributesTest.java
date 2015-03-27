package ch.epfl.imhof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

public class AttributesTest {

	private HashMap<String, String> sampleAttributesValues() {
		HashMap<String, String> testData = new HashMap<>();
		testData.put("testKey 1", "testValue 1");
		testData.put("testKey 2", "testValue 2");
		testData.put("testKey 3", "testValue 3");
		return testData;
	}

	@Test
	public void constructorAndNonMutableAttributes() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		testData.put("testKey 4", "testValue 4");
		assertTrue(testData.containsKey("testKey 4"));
		assertFalse(testAttributes.contains("testKey 4"));
	}

	@Test
	public void emptyAttributesIndication() {
		Attributes testAttributes = new Attributes(new HashMap<>());
		assertTrue(testAttributes.isEmpty());
	}

	@Test
	public void attributesIsEmptyWhenNotEmpty() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		assertFalse(testAttributes.isEmpty());
	}

	@Test
	public void containedKeys() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		assertTrue(testAttributes.contains("testKey 1")
				&& testAttributes.contains("testKey 2")
				&& testAttributes.contains("testKey 3"));
		assertFalse(testAttributes.contains("testKey 4"));
	}

	@Test
	public void getTheValue() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		assertEquals("testValue 1", testAttributes.get("testKey 1"));
		assertEquals("testValue 2", testAttributes.get("testKey 2"));
		assertEquals("testValue 3", testAttributes.get("testKey 3"));
		assertEquals(null, testAttributes.get("testKey 4"));
	}

	@Test
	public void getWhereNoSuch() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		assertEquals("testValue 1", testAttributes.get("testKey 1", "No Such"));
		assertEquals("No Such", testAttributes.get("testKey 4", "No Such"));
	}

	@Test
	public void getIntFromString() {
		HashMap<String, String> testData = new HashMap<>();
		testData.put("testKey 1", "-81");
		testData.put("testKey 2", "42");
		testData.put("testKey 3", "yes");
		testData.put("testKey 5", null);
		Attributes testAttributes = new Attributes(testData);
		assertEquals(-81, testAttributes.get("testKey 1", 0));
		assertEquals(42, testAttributes.get("testKey 2", 0));
		assertEquals(0, testAttributes.get("testKey 3", 0));
		assertEquals(0, testAttributes.get("testKey 4", 0));
		assertEquals(0, testAttributes.get("testKey 5", 0));
	}

	@Test
	public void builderBuilt() {
		Attributes.Builder testBuild = new Attributes.Builder();
		testBuild.put("testKey 1", "testValue 1");
		testBuild.put("testKey 2", "testValue 2");
		testBuild.put("testKey 3", "testValue 3");
		Attributes test = testBuild.build();
		assertTrue(test.contains("testKey 1") && test.contains("testKey 2")
				&& test.contains("testKey 3"));
		assertEquals(test.get("testKey 1"), "testValue 1");
		assertEquals(test.get("testKey 2"), "testValue 2");
		assertEquals(test.get("testKey 3"), "testValue 3");
		testBuild.put("testKey 4", "testValue 4");
		assertFalse(test.contains("testKey 4"));
	}

	@Test
	public void keysWithEmptySet() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		HashSet<String> keptKeys = new HashSet<>();
		Attributes testResult = testAttributes.keepOnlyKeys(keptKeys);
		assertTrue(testResult.isEmpty());
	}

	@Test
	public void someKeysKeepingFromInitial() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		HashSet<String> keptKeys = new HashSet<>();
		keptKeys.add("testKey 1");
		keptKeys.add("testKey 3");
		Attributes testResult = testAttributes.keepOnlyKeys(keptKeys);
		assertTrue(testResult.contains("testKey 1")
				&& testResult.contains("testKey 3")
				&& !testResult.contains("testKey 2"));
	}

	@Test
	public void allKeysKeepingFromInitial() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		HashSet<String> keptKeys = new HashSet<>();
		keptKeys.add("testKey 1");
		keptKeys.add("testKey 2");
		keptKeys.add("testKey 3");
		keptKeys.add("testKey 4");
		Attributes testResult = testAttributes.keepOnlyKeys(keptKeys);
		assertTrue(testResult.contains("testKey 1")
				&& testResult.contains("testKey 2")
				&& testResult.contains("testKey 3")
				&& !testResult.contains("testKey 4"));
	}

	@Test
	public void keysWithKeyNotInMap() {
		HashMap<String, String> testData = sampleAttributesValues();
		Attributes testAttributes = new Attributes(testData);
		HashSet<String> keptKeys = new HashSet<>();
		keptKeys.add("testKey 4");
		Attributes testResult = testAttributes.keepOnlyKeys(keptKeys);
		assertTrue(testResult.isEmpty());
	}

}
