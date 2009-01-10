package com.googlecode.jau;

import com.googlecode.jau.equals.EqualsAnnotatedThroughPackage;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for JAU.toString()
 */
public class ToStringTest {
    private String identityToString(Object a) {
        return a.getClass().getName() + "@" + Integer.toHexString(
                System.identityHashCode(a));
    }

    @Test
    public void emptyNoAnnotation() {
        EmptyNoAnnotation a = new EmptyNoAnnotation();
        assertTrue(JAU.toString(a).equals(
                "com.googlecode.jau.EmptyNoAnnotation@" + 
                Integer.toHexString(System.identityHashCode(a))));
    }

    @Test
    public void oneFieldNoAnnotation() {
        OneFieldNoAnnotation a = new OneFieldNoAnnotation();
        assertTrue(JAU.toString(a).equals(
                "com.googlecode.jau.OneFieldNoAnnotation@" +
                Integer.toHexString(System.identityHashCode(a))));
    }

    @Test
    public void empty() {
        Empty a = new Empty();
        assertEquals(
                "com.googlecode.jau.Empty@" +
                Integer.toHexString(System.identityHashCode(a)) + "()",
                JAU.toString(a));
    }

    @Test
    public void includeFalse() {
        IncludeFalse a = new IncludeFalse();
        a.value = 27;
        assertEquals(
                "com.googlecode.jau.IncludeFalse@" +
                Integer.toHexString(System.identityHashCode(a)) + "()",
                JAU.toString(a));
    }

    @Test
    public void oneField() {
        OneField a = new OneField();
        a.value = 27;
        assertEquals(
                "com.googlecode.jau.OneField@" +
                Integer.toHexString(System.identityHashCode(a)) + "(value=27)",
                JAU.toString(a));
    }

    @Test
    public void arrayField() {
        ArrayField a = new ArrayField();
        a.value = null;
        assertEquals(identityToString(a) + "(value=null)",
                JAU.toString(a));

        a.value = new int[0];
        assertEquals(identityToString(a) + "(value=int[])",
                JAU.toString(a));

        a.value = new int[] {27, 34};
        assertEquals(identityToString(a) + "(value=int[27, 34])",
                JAU.toString(a));
    }

    @Test
    public void privateField() {
        PrivateField a = new PrivateField();
        a.setValue(20);
        assertEquals(identityToString(a) + "(value=20)",
                JAU.toString(a));
    }

    @Test
    public void protectedField() {
        ProtectedField a = new ProtectedField();
        a.value = 32;
        assertEquals(identityToString(a) + "(value=32)",
                JAU.toString(a));
    }

    @Test
    public void inherited() {
        AllFields2 a = new AllFields2();
        a.value = 32;
        a.value2 = 33;
        assertEquals(identityToString(a) + "(value2=33, value=32)",
                JAU.toString(a));
    }

    @Test
    public void annotatedThroughPackage() {
        EqualsAnnotatedThroughPackage a = new EqualsAnnotatedThroughPackage();
        a.value = 32;
        assertEquals(identityToString(a) + "(value=32)",
                JAU.toString(a));
    }

    @Test
    public void object() {
        Object a = new Object();
        assertEquals(identityToString(a), JAU.toString(a));
    }

    @Test
    public void staticField() {
        StaticField a = new StaticField();
        assertEquals(identityToString(a) + "()", JAU.toString(a));
    }

    @Test
    public void double_() {
        assertEquals("0.0", JAU.toString(new Double(0)));
        assertEquals("1.0", JAU.toString(new Double(1)));
        assertEquals("NaN", JAU.toString(new Double(Double.NaN)));
        assertEquals("-Infinity", JAU.toString(new Double(Double.NEGATIVE_INFINITY)));
        assertEquals("Infinity", JAU.toString(new Double(Double.POSITIVE_INFINITY)));
    }

    @Test
    public void bigDecimal() {
        assertEquals("0", JAU.toString(new BigDecimal(0)));
        assertEquals("1.1", JAU.toString(new BigDecimal("1.1")));
    }

    @Test
    public void array() {
        assertEquals("java.lang.String[]", JAU.toString(new String[0]));
        assertEquals("int[]", JAU.toString(new int[0]));
        assertEquals("java.lang.String[\"a\"]", JAU.toString(new String[] {"a"}));
        assertEquals("java.lang.String[\"a\", \"b\"]",
                JAU.toString(new String[] {"a", "b"}));
        assertEquals("java.lang.String[][java.lang.String[\"a\", \"b\"], " +
                "java.lang.String[\"1\", \"2\"]]",
                JAU.toString(new String[][] {{"a", "b"}, {"1", "2"}}));
    }

    @Test
    public void null_() {
        assertEquals("null", JAU.toString(null));
    }
}
