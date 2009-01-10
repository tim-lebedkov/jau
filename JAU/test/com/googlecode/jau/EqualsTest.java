package com.googlecode.jau;

import com.googlecode.jau.equals.EqualsAnnotatedThroughPackage;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for JAU
 */
public class EqualsTest {
    private static final class EmptyNoAnnotation {
    }

    private static final class OneFieldNoAnnotation {
        public int value;
    }

    @JAUEquals
    private static final class Empty {
    }

    @JAUEquals
    private static final class IncludeFalse {
        @JAUEquals(include=false)
        public int value;
    }

    @JAUEquals
    private static final class OneField {
        public int value;
    }

    @JAUEquals
    private static final class ArrayField {
        public int[] value;
    }

    @JAUEquals
    private static final class PrivateField {
        private int value;
    }

    @JAUEquals
    private static final class ProtectedField {
        protected int value;
    }

    @JAUEquals
    private static class AllFields {
        protected int value;
    }

    @JAUEquals
    private static final class AllFields2 extends AllFields {
        protected int value2;
    }

    @JAUEquals
    private static class StaticField {
        public static Object a = new Object();
    }

    @org.junit.Test
    public void empty() {
        EmptyNoAnnotation a = new EmptyNoAnnotation(),
                b = new EmptyNoAnnotation();
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void oneFieldNoAnnotation() {
        OneFieldNoAnnotation a = new OneFieldNoAnnotation(),
                b = new OneFieldNoAnnotation();
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void diffClasses() {
        OneFieldNoAnnotation a = new OneFieldNoAnnotation();
        EmptyNoAnnotation b = new EmptyNoAnnotation();
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void emptyEq() {
        Empty a = new Empty(), b = new Empty();
        assertTrue(JAU.equals(a, b));
    }

    @org.junit.Test
    public void includeFalse() {
        IncludeFalse a = new IncludeFalse(), b = new IncludeFalse();
        a.value = 0;
        b.value = 1;
        assertTrue(JAU.equals(a, b));
    }

    @org.junit.Test
    public void oneField() {
        OneField a = new OneField(), b = new OneField();
        assertTrue(JAU.equals(a, b));
        a.value = 0;
        b.value = 1;
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void arrayField() {
        ArrayField a = new ArrayField(), b = new ArrayField();
        assertTrue(JAU.equals(a, b));

        a.value = new int[] {0};
        b.value = new int[] {0};
        assertTrue(JAU.equals(a, b));

        a.value = new int[] {0};
        b.value = new int[] {1};
        assertFalse(JAU.equals(a, b));

        a.value = null;
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void privateField() {
        PrivateField a = new PrivateField(), b = new PrivateField();
        assertTrue(JAU.equals(a, b));

        a.value = 0;
        b.value = 1;
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void protectedField() {
        ProtectedField a = new ProtectedField(), b = new ProtectedField();
        assertTrue(JAU.equals(a, b));

        a.value = 0;
        b.value = 1;
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void allFields() {
        AllFields a = new AllFields(), b = new AllFields();
        assertTrue(JAU.equals(a, b));

        a.value = 0;
        b.value = 1;
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void inherited() {
        AllFields2 a = new AllFields2(), b = new AllFields2();
        assertTrue(JAU.equals(a, b));

        a.value = 0;
        b.value = 1;
        assertFalse(JAU.equals(a, b));

        a.value = 0;
        b.value = 0;
        a.value2 = 1;
        b.value2 = 2;
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void annotatedThroughPackage() {
        EqualsAnnotatedThroughPackage a = new EqualsAnnotatedThroughPackage();
        EqualsAnnotatedThroughPackage b = new EqualsAnnotatedThroughPackage();

        assertTrue(JAU.equals(a, b));

        a.value = 0;
        b.value = 1;
        assertFalse(JAU.equals(a, b));
    }

    @Test
    public void object() {
        assertFalse(JAU.equals(new Object(), new Object()));
    }

    @Test
    public void staticField() {
        assertTrue(JAU.equals(new StaticField(), new StaticField()));
    }

    @Test
    public void doubleEquals() {
        assertTrue(JAU.equals(new Double(0), new Double(0)));
        assertFalse(JAU.equals(new Double(1), new Double(1.1)));
        assertTrue(JAU.equals(new Double(Double.NaN), new Double(Double.NaN)));
        assertTrue(JAU.equals(new Double(Double.NEGATIVE_INFINITY),
                new Double(Double.NEGATIVE_INFINITY)));
        assertFalse(JAU.equals(new Double(Double.POSITIVE_INFINITY),
                new Double(Double.NEGATIVE_INFINITY)));
    }

    @Test
    public void bigDecimal() {
        assertTrue(JAU.equals(new BigDecimal(0), new BigDecimal(0)));
        assertFalse(JAU.equals(new BigDecimal(1), new BigDecimal(1.1)));
    }

    @Test
    public void array() {
        assertTrue(JAU.equals(new String[0], new String[0]));
        assertFalse(JAU.equals(new String[0], new int[0]));
        assertTrue(JAU.equals(new String[] {"a"}, new String[] {"a"}));
        assertFalse(JAU.equals(new String[] {"a"}, new String[] {"b"}));
        assertFalse(JAU.equals(new String[] {"a"}, new String[] {"a", "b"}));
        assertFalse(JAU.equals(new String[] {"a"}, new String[] {}));
        assertFalse(JAU.equals(new String[] {"a"}, new String[][] {}));
        assertTrue(JAU.equals(new String[][] {{"a", "b"}, {"1", "2"}},
                new String[][] {{"a", "b"}, {"1", "2"}}));
    }
}
