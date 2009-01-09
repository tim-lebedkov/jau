package com.googlecode.jau;

import com.googlecode.jau.EqualsClass;
import com.googlecode.jau.JAU;
import com.googlecode.jau.EqualsProperty;
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

    @EqualsClass
    private static final class Empty {
    }

    @EqualsClass
    private static final class OneField {
        public int value;
    }

    @EqualsClass
    private static final class OneField2 {
        @EqualsProperty
        public int value;
    }

    @EqualsClass
    private static final class ArrayField {
        @EqualsProperty
        public int[] value;
    }

    @EqualsClass
    private static final class PrivateField {
        @EqualsProperty
        private int value;
    }

    @EqualsClass
    private static final class ProtectedField {
        @EqualsProperty
        protected int value;
    }

    @EqualsClass(allFields=true)
    private static class AllFields {
        protected int value;
    }

    @EqualsClass(allFields=true, inherited=true)
    private static final class AllFields2 extends AllFields {
        protected int value2;
    }

    @org.junit.Test
    public void empty() {
        EmptyNoAnnotation a = new EmptyNoAnnotation(),
                b = new EmptyNoAnnotation();
        assertFalse(JAU.equals(a, b));
    }

    @org.junit.Test
    public void oneField() {
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
    public void oneFieldEq() {
        OneField a = new OneField(), b = new OneField();
        a.value = 0;
        b.value = 1;
        assertTrue(JAU.equals(a, b));
    }

    @org.junit.Test
    public void oneFieldEq2() {
        OneField2 a = new OneField2(), b = new OneField2();
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
}
