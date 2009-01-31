package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Test modelled after
 * http://java.dzone.com/tips/refactor-safe-tostringbuilder.
 */
@JAUToString
public class PerfTest extends JapexDriverBase {
    private int x = 1;
    private int y = 2;
    private int z = 3;
    private double a = 5.4343;
    private String txt = "Testme";

    public void run(TestCase testCase) {
        JAU.toString(this);
    }

    public static void main(String[] params) {
        PerfTest a = new PerfTest();
        PerfTest b = new PerfTest();
        b.x = 2;
        for (int i = 0; i < 10000000; i++) {
            //JAU.toString(a);
            JAU.equals(a, b);
        }
    }
}