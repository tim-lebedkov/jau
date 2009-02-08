package com.googlecode.jau;

import com.googlecode.jau.equals.EqualsAnnotatedThroughPackage;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for JAU.equals and hashCode.
 */
public class EqualsTest {
    /**
     * Checks equals(), hashCode() and clone() for 2 equal objects.
     *
     * @param a first object or null
     * @param b second object or null
     */
    private static void ensureEqual(Object a, Object b) {
        ensureEqual(a, b, true, false);
    }

    /**
     * Checks equals() and hashCode() for 2 equal objects.
     *
     * @param a first object or null
     * @param b second object or null
     * @param clone true = test clone() too
     * @param uncomparable true = JAU.compare() should throw an
     *     IllegalArgumentException, false = 0 should be returned
     */
    private static void ensureEqual(Object a, Object b, boolean clone,
            boolean uncomparable) {
        assertTrue(JAU.equals(a, b));
        assertEquals(JAU.hashCode(a), JAU.hashCode(b));
        if (uncomparable) {
            try {
                assertEquals(0, JAU.compare(a, b));
                assertTrue("IllegalArgumentException expected", false);
            } catch (IllegalArgumentException ex) {
                // OK. As expected.
            }
        } else {
            assertEquals(0, JAU.compare(a, b));
        }
        if (clone) {
            assertTrue(JAU.equals(a, JAU.clone(a)));
            assertTrue(JAU.equals(b, JAU.clone(b)));
            assertTrue(JAU.equals(a, JAU.clone(b)));
            assertTrue(JAU.equals(b, JAU.clone(a)));
        }
    }

    /**
     * Checks equals(), hashCode() and clone() for 2 equal objects.
     *
     * @param a first object or null
     * @param b second object or null
     */
    private static void ensureUnequal(Object a, Object b) {
        ensureUnequal(a, b, true, false);
    }

    /**
     * Checks equals() and hashCode() for 2 equal objects.
     *
     * @param a first object or null
     * @param b second object or null
     * @param testCopy true = test JAU.clone() too
     * @param uncomparable if true, an IllegalArgumentException should be
     *     thrown by JAU.compare(), otherwise only a value different from 0
     *     should be returned
     */
    private static void ensureUnequal(Object a, Object b, boolean testCopy,
            boolean uncomparable) {
        assertFalse(JAU.equals(a, b));

        // for simple tests this should never happen
        assertFalse(JAU.hashCode(a) == JAU.hashCode(b));

        if (uncomparable) {
            try {
                assertTrue(JAU.compare(a, b) != 0);
                assertTrue("IllegalArgumentException was expected", false);
            } catch (IllegalArgumentException ex) {
                // OK. As expected
            }
        } else {
            assertTrue(JAU.compare(a, b) != 0);
        }

        if (testCopy) {
            assertFalse(JAU.equals(a, JAU.clone(b)));
            assertFalse(JAU.equals(b, JAU.clone(a)));
        }
    }

    @org.junit.Test
    public void emptyNoAnnotation() {
        EmptyNoAnnotation a = new EmptyNoAnnotation();
        EmptyNoAnnotation b = new EmptyNoAnnotation();
        ensureUnequal(a, b, false, true);
    }

    @org.junit.Test
    public void oneFieldNoAnnotation() {
        OneFieldNoAnnotation a = new OneFieldNoAnnotation();
        OneFieldNoAnnotation b = new OneFieldNoAnnotation();
        ensureUnequal(a, b, false, true);
    }

    @org.junit.Test
    public void diffClasses() {
        OneFieldNoAnnotation a = new OneFieldNoAnnotation();
        EmptyNoAnnotation b = new EmptyNoAnnotation();
        ensureUnequal(a, b, false, true);
    }

    @org.junit.Test
    public void emptyEq() {
        Empty a = new Empty();
        Empty b = new Empty();
        ensureEqual(a, b);
    }

    @org.junit.Test
    public void includeFalse() {
        IncludeFalse a = new IncludeFalse();
        IncludeFalse b = new IncludeFalse();
        a.value = 0;
        b.value = 1;
        ensureEqual(a, b);
    }

    @org.junit.Test
    public void oneField() {
        OneField a = new OneField();
        OneField b = new OneField();
        ensureEqual(a, b);
        a.value = 0;
        b.value = 1;
        ensureUnequal(a, b);
    }

    @org.junit.Test
    public void arrayField() {
        ArrayField a = new ArrayField();
        ArrayField b = new ArrayField();
        ensureEqual(a, b);

        a.value = new int[] {0};
        b.value = new int[] {0};
        ensureEqual(a, b);

        a.value = new int[] {0};
        b.value = new int[] {1};
        ensureUnequal(a, b);

        a.value = null;
        ensureUnequal(a, b);
    }

    @org.junit.Test
    public void privateField() {
        PrivateField a = new PrivateField();
        PrivateField b = new PrivateField();
        ensureEqual(a, b);

        a.setValue(0);
        b.setValue(1);
        ensureUnequal(a, b);
    }

    @org.junit.Test
    public void protectedField() {
        ProtectedField a = new ProtectedField();
        ProtectedField b = new ProtectedField();
        ensureEqual(a, b);

        a.value = 0;
        b.value = 1;
        ensureUnequal(a, b);
    }

    @org.junit.Test
    public void allFields() {
        AllFields a = new AllFields();
        AllFields b = new AllFields();
        ensureEqual(a, b);

        a.value = 0;
        b.value = 1;
        ensureUnequal(a, b);
    }

    @org.junit.Test
    public void inherited() {
        AllFields2 a = new AllFields2();
        AllFields2 b = new AllFields2();
        ensureEqual(a, b);

        a.value = 0;
        b.value = 1;
        ensureUnequal(a, b);

        a.value = 0;
        b.value = 0;
        a.value2 = 1;
        b.value2 = 2;
        ensureUnequal(a, b);
    }

    @org.junit.Test
    public void annotatedThroughPackage() {
        EqualsAnnotatedThroughPackage a = new EqualsAnnotatedThroughPackage();
        EqualsAnnotatedThroughPackage b = new EqualsAnnotatedThroughPackage();

        ensureEqual(a, b);

        a.value = 0;
        b.value = 1;
        ensureUnequal(a, b);
    }

    @Test
    public void object() {
        ensureUnequal(new Object(), new Object(), true, true);
        Object a = new Object();
        ensureEqual(a, a);
    }

    @Test
    public void staticField() {
        ensureEqual(new StaticField(), new StaticField());
    }

    @Test
    public void doubleEquals() {
        ensureEqual(new Double(0), new Double(0));
        ensureUnequal(new Double(1), new Double(1.1));
        ensureEqual(new Double(Double.NaN), new Double(Double.NaN));
        ensureEqual(new Double(Double.NEGATIVE_INFINITY),
                new Double(Double.NEGATIVE_INFINITY));
        ensureUnequal(new Double(Double.POSITIVE_INFINITY),
                new Double(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void bigDecimal() {
        ensureEqual(new BigDecimal(0), new BigDecimal(0));
        ensureUnequal(new BigDecimal(1), new BigDecimal(1.1));
    }

    @Test
    public void array() {
        ensureEqual(new String[0], new String[0]);
        ensureUnequal(new String[0], new int[0], true, true);
        ensureEqual(new String[] {"a"}, new String[] {"a"});
        ensureUnequal(new String[] {"a"}, new String[] {"b"});
        ensureUnequal(new String[] {"a"}, new String[] {"a", "b"});
        ensureUnequal(new String[] {"a"}, new String[] {});
        ensureUnequal(new String[] {"a"}, new String[][] {}, true, true);
        ensureEqual(new String[][] {{"a", "b"}, {"1", "2"}},
                new String[][] {{"a", "b"}, {"1", "2"}});
    }

    @Test
    public void nulls() {
        ensureEqual(null, null);
        ensureUnequal(null, "");
        ensureUnequal("", null);
    }

    @Test
    public void hashMap() {
        HashMap a = new HashMap();
        a.put("value", new int[0]);
        HashMap b = new HashMap();
        b.put("value", new int[0]);
        ensureEqual(a, b, false, true);
    }

    @Test
    public void enum_() {
        ensureEqual(ColorsEnum.RED, ColorsEnum.RED);
        ensureUnequal(ColorsEnum.RED, ColorsEnum.GREEN);
    }

    @Test
    public void stringBuffer() {
        ensureEqual(new StringBuffer("a"), new StringBuffer("a"));
        ensureUnequal(new StringBuffer("a"), new StringBuffer("b"));
    }

    @Test
    public void vector() {
        Vector a = new Vector();
        a.add("test");
        a.add(new Integer(2546));
        a.add(ColorsEnum.GREEN);

        Vector b = new Vector();
        b.add("test");
        b.add(new Integer(2546));
        b.add(ColorsEnum.GREEN);

        ensureEqual(a, b, false, false);

        b.set(0, "test2");
        ensureUnequal(a, b, false, false);
    }
}
