package com.googlecode.jau;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for JAU.compare
 */
public class CompareTest {
    @Test
    public void oneField() {
        OneField a = new OneField();
        OneField b = new OneField();
        assertEquals(0, JAU.compare(a, b));

        a.value = 0;
        b.value = 1;
        assertEquals(-1, JAU.compare(a, b));

        a.value = 2;
        b.value = 1;
        assertEquals(1, JAU.compare(a, b));
    }
    
    @Test
    public void inherited() {
        AllFields2 a = new AllFields2();
        AllFields2 b = new AllFields2();
        assertEquals(0, JAU.compare(a, b));

        a.value = 0;
        b.value = 1;
        assertEquals(-1, JAU.compare(a, b));

        a.value = 0;
        b.value = 0;
        a.value2 = 1;
        b.value2 = 2;
        assertEquals(-1, JAU.compare(a, b));

        a.value = 2;
        b.value = 0;
        a.value2 = 1;
        b.value2 = 2;
        assertEquals(1, JAU.compare(a, b));
    }

    @Test
    public void null_(){
        assertEquals(0, JAU.compare(null, null));
        assertEquals(-1, JAU.compare(null, new Integer(0)));
        assertEquals(1, JAU.compare(new Integer(1), null));
    }

    @Test
    public void string() {
        assertEquals(0, JAU.compare("a", "a"));
        assertEquals(-1, JAU.compare("a", "b"));
        assertEquals(1, JAU.compare("b2", "b"));
    }

    @Test
    public void double_() {
        assertEquals(0, JAU.compare(new Double(11), new Double(11)));
        assertEquals(-1, JAU.compare(new Double(-1), new Double(1000)));
        assertEquals(1, JAU.compare(new Double(2000), new Double(999)));
    }

    @Test
    public void bigDecimal() {
        assertEquals(0, JAU.compare(new BigDecimal(11), new BigDecimal(11)));
        assertEquals(-1, JAU.compare(new BigDecimal(-1), new BigDecimal(1000)));
        assertEquals(1, JAU.compare(new BigDecimal(2000), new BigDecimal(999)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void arrayUncomparable() {
        assertEquals(-1, JAU.compare(new int[] {},
                new String[] {}));
    }

    @Test
    public void array() {
        assertEquals(0, JAU.compare(new int[] {},
                new int[] {}));
        assertEquals(0, JAU.compare(new int[] {1, 2, 3, 4},
                new int[] {1, 2, 3, 4}));
        assertEquals(-1, JAU.compare(new int[] {1, 2, 3, 3},
                new int[] {1, 2, 3, 4}));
        assertEquals(1, JAU.compare(new int[] {1, 2, 3, 5},
                new int[] {1, 2, 3, 4}));
        assertEquals(-1, JAU.compare(new int[] {1, 2, 3},
                new int[] {1, 2, 3, 4}));
        assertEquals(1, JAU.compare(new int[] {1, 2, 3, 4},
                new int[] {1, 2, 3}));
    }
}
