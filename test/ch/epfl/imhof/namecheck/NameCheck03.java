package ch.epfl.imhof.namecheck;

import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;

public class NameCheck03 {
    @Test
    @Ignore
    public void useAllStage03Names() {
        // Attributes and Attributes.Builder
        Attributes.Builder ab = new Attributes.Builder();
        ab.put("key", "value");
        Attributes as = ab.build();
        as = new Attributes(Collections.<String, String>emptyMap());
        boolean b = as.isEmpty();
        b = as.contains("");
        String s = as.get("");
        s = as.get(s, s);
        int i = as.get("", 0);
        as = as.keepOnlyKeys(Collections.<String>emptySet());

        // Attributed
        Attributed<Boolean> ad = new Attributed<>(b, as);
        b = ad.value();
        as = ad.attributes();
        b = ad.hasAttribute(s);
        s = ad.attributeValue(s);
        s = ad.attributeValue(s, s);
        i = ad.attributeValue(s, i);
    }
}
