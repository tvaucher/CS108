package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
public class AttributesTest {

    Map<String, String> map1 = new HashMap<>();
    Map<String, String> map2 = new HashMap<String, String>();
    
    @Test
    public void attributesAreClonedBeforeBeingStored() {
        map1.put("number", "1");
        map1.put("string", "test");
        map1.put("hex", "FF");
        Attributes inst1 = new Attributes(map1);
        Attributes inst2 = new Attributes(map2);
        
        assertTrue(inst1.contains("string"));
        map1.clear();
        assertTrue(inst1.contains("string"));
        
        assertFalse(inst2.contains("string"));
        map2.put("string", "test");
        assertFalse(inst2.contains("string"));
    }
        
    @Test
    public void isEmptyReturnsCorrectValues() {
        Attributes.Builder attributes1 = new Attributes.Builder();
        attributes1.put("number", "1");
        attributes1.put("string", "test");
        attributes1.put("hex", "FF");
        Attributes inst1 = attributes1.build();
        Attributes inst2 = new Attributes.Builder().build();
        
        assertFalse(inst1.isEmpty());
        assertTrue(inst2.isEmpty());
    }
    
    @Test
    public void getWorksInAllCases() {
        Attributes.Builder attributes1 = new Attributes.Builder();
        attributes1.put("number", "1");
        attributes1.put("string", "test");
        attributes1.put("hex", "FF");
        Attributes inst1 = attributes1.build();
        Attributes inst2 = new Attributes.Builder().build();
        
        // Filled list
        // In the list:
        assertEquals(inst1.get("number"), "1");
        assertEquals(inst1.get("number", "0"), "1");
        assertEquals(inst1.get("number", 0), 1);
        // Check that it doesn't return 255 for some obscure reason (it doesn't):
        assertEquals(inst1.get("hex", 0), 0);

        // Not in the list:
        assertEquals(inst1.get("notinlist"), null);
        assertEquals(inst1.get("notinlist", "0"), "0");
        assertEquals(inst1.get("notinlist", 0), 0);
        assertEquals(inst1.get(""), null);
        assertEquals(inst1.get("", 0), 0);
        assertEquals(inst1.get("", "0"), "0");
        
        //Empty list
        assertEquals(inst2.get("notinlist"), null);
        assertEquals(inst2.get("notinlist", 0), 0);
        assertEquals(inst2.get("notinlist", "0"), "0");
        assertEquals(inst2.get(""), null);
        assertEquals(inst2.get("", 0), 0);
        assertEquals(inst2.get("", "0"), "0");
    }
    
    @Test
    public void keepOnlyKeysReturnsDifferentElement() {
        Attributes.Builder attributes1 = new Attributes.Builder();
        attributes1.put("number", "1");
        attributes1.put("string", "test");
        attributes1.put("hex", "FF");
        Attributes inst1 = attributes1.build();
        Attributes inst2 = new Attributes.Builder().build();
        
        Set<String> keys1 = new TreeSet<>();
        Set<String> keys2 = new TreeSet<>();
        keys1.add("number");
        keys1.add("string");
        keys1.add("hex");
        
        assertNotSame(inst1, inst1.keepOnlyKeys(keys1));
        assertNotSame(inst2, inst2.keepOnlyKeys(keys2));
    }

    
    @Test
    public void keepOnlyKeysReturnsCorrectObject() {
        Attributes.Builder attributes1 = new Attributes.Builder();
        attributes1.put("number", "1");
        attributes1.put("string", "test");
        attributes1.put("hex", "FF");
        Attributes inst1 = attributes1.build();
        Attributes inst2 = new Attributes.Builder().build();
        
        Set<String> keys1 = new TreeSet<>();
        Set<String> keys2 = new TreeSet<>();
        keys1.add("number");
        keys1.add("string");
        keys1.add("hex");
        
        assertFalse(inst1.keepOnlyKeys(keys1).isEmpty());
        assertTrue(inst1.keepOnlyKeys(keys2).isEmpty());
        assertTrue(inst2.keepOnlyKeys(keys1).isEmpty());
        assertTrue(inst2.keepOnlyKeys(keys2).isEmpty());
        
        assertTrue(inst1.keepOnlyKeys(keys1).contains("number"));
        assertTrue(inst1.keepOnlyKeys(keys1).contains("string"));
        assertTrue(inst1.keepOnlyKeys(keys1).contains("hex"));
    }
}
