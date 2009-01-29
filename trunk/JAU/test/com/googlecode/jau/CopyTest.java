package com.googlecode.jau;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for JAU.copy
 */
public class CopyTest {
    @Test(expected=IllegalArgumentException.class)
    public void enum_() {
        JAU.copy(ColorsEnum.BLUE, ColorsEnum.RED);
    }

    @Test(expected=IllegalArgumentException.class)
    public void arrayUnequalLengths() {
        JAU.copy(new int[] {2}, new int[] {2, 3});
    }

    @Test(expected=IllegalArgumentException.class)
    public void null1() {
        JAU.copy(null, new int[] {2, 3});
    }

    @Test(expected=IllegalArgumentException.class)
    public void null2() {
        JAU.copy(new int[] {2, 3}, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullBoth() {
        JAU.copy(null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void differentClasses() {
        JAU.copy(new AllFields(), new AllFields2());
    }

    @Test
    public void stringBuffer() {
        StringBuffer a = new StringBuffer();
        StringBuffer b = new StringBuffer();
        a.append("test");
        JAU.copy(a, b);
        assertEquals("test", b.toString());
    }

    @Test
    public void stringBuilder() {
        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();
        a.append("test");
        JAU.copy(a, b);
        assertEquals("test", b.toString());
    }

    @Test(expected=IllegalArgumentException.class)
    public void class_() {
        JAU.copy(String.class, Integer.class);
    }
}
