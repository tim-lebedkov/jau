package com.googlecode.jau;

import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for JAU.toMap()
 */
public class ToMapTest {
    @Test
    public void emptyNoAnnotation() {
        EmptyNoAnnotation a = new EmptyNoAnnotation();
        assertEquals(new HashMap(), JAU.toMap(a));
    }

    @Test
    public void oneFieldNoAnnotation() {
        OneFieldNoAnnotation a = new OneFieldNoAnnotation();
        assertEquals(new HashMap(), JAU.toMap(a));
    }

    @Test
    public void empty() {
        Empty a = new Empty();
        assertEquals(new HashMap(), JAU.toMap(a));
    }

    @Test
    public void includeFalse() {
        IncludeFalse a = new IncludeFalse();
        a.value = 27;
        assertEquals(new HashMap(), JAU.toMap(a));
    }

    @Test
    public void oneField() {
        OneField a = new OneField();
        a.value = 27;
        HashMap m = new HashMap();
        m.put("value", new Integer(27));
        assertEquals(m, JAU.toMap(a));
    }

    @Test
    public void arrayField() {
        ArrayField a = new ArrayField();
        a.value = null;
        HashMap m = new HashMap();
        m.put("value", null);
        assertEquals(m, JAU.toMap(a));

        a.value = new int[0];
        m = new HashMap();
        m.put("value", new int[0]);
        assertTrue(JAU.equals(m, JAU.toMap(a)));

        a.value = new int[] {27, 34};
        m = new HashMap();
        m.put("value", new int[] {27, 34});
        assertTrue(JAU.equals(m, JAU.toMap(a)));
    }
}
