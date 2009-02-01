package com.googlecode.jau;

import com.googlecode.jau.equals.EqualsAnnotatedThroughPackage;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for JAU.toMap()
 */
public class ToMapTest {
    @Test(expected=IllegalArgumentException.class)
    public void emptyNoAnnotation() {
        EmptyNoAnnotation a = new EmptyNoAnnotation();
        assertEquals(new HashMap(), JAU.toMap(a));
    }

    @Test(expected=IllegalArgumentException.class)
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

    @Test
    public void privateField() {
        PrivateField a = new PrivateField();
        a.setValue(20);
        HashMap m = new HashMap();
        m.put("value", new Integer(20));
        assertEquals(m, JAU.toMap(a));
    }

    @Test
    public void protectedField() {
        ProtectedField a = new ProtectedField();
        a.value = 32;
        HashMap m = new HashMap();
        m.put("value", new Integer(32));
        assertEquals(m, JAU.toMap(a));
    }

    @Test
    public void inherited() {
        AllFields2 a = new AllFields2();
        a.value = 32;
        a.value2 = 33;
        HashMap m = new HashMap();
        m.put("value", new Integer(32));
        m.put("value2", new Integer(33));
        assertEquals(m, JAU.toMap(a));
    }

    @Test
    public void annotatedThroughPackage() {
        EqualsAnnotatedThroughPackage a = new EqualsAnnotatedThroughPackage();
        a.value = 32;
        HashMap m = new HashMap();
        m.put("value", new Integer(32));
        assertEquals(m, JAU.toMap(a));
    }

    @Test(expected=IllegalArgumentException.class)
    public void object() {
        Object a = new Object();
        HashMap m = new HashMap();
        assertEquals(m, JAU.toMap(a));
    }

    @Test(expected=IllegalArgumentException.class)
    public void integer() {
        HashMap m = new HashMap();
        assertEquals(m, JAU.toMap(new Integer(0)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void string() {
        HashMap m = new HashMap();
        assertEquals(m, JAU.toMap("s"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void bigDecimal() {
        HashMap m = new HashMap();
        assertEquals(m, JAU.toMap(new BigDecimal(1023)));
    }

    @Test
    public void null_() {
        HashMap m = new HashMap();
        assertEquals(m, JAU.toMap(null));
    }

    @Test
    public void staticField() {
        StaticField a = new StaticField();
        HashMap m = new HashMap();
        assertEquals(m, JAU.toMap(a));
    }

    @Test
    public void twoClasses() {
        ClassOne a = new ClassOne();
        ClassTwo b = new ClassTwo();

        Map map = JAU.toMap(a);
        JAU.fromMap(map, b);

        map = JAU.toMap(b);

        ClassOne a2 = new ClassOne();
        a2.clear();
        assertEquals(0, a2.byte_);
        JAU.fromMap(map, a2);
        assertTrue(JAU.toString(a) + " <> " + JAU.toString(a2), JAU.equals(a, a2));
    }

    @Test(expected=IllegalArgumentException.class)
    public void enum_() {
        Map map = JAU.toMap(ColorsEnum.BLUE);
        System.out.println(JAU.toString(map));
    }

    @Test(expected=IllegalArgumentException.class)
    public void array() {
        Map map = JAU.toMap(new int[] {0});
        System.out.println(JAU.toString(map));
    }
}
