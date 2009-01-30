package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Test modelled after
 * http://java.dzone.com/tips/refactor-safe-tostringbuilder.
 */
@JAUToString
public class GGJAUPerfTest2 extends JapexDriverBase {
    private int x = 1;
    private int y = 2;
    private int z = 3;
    private double a = 5.4343;
    private String txt = "Testme";

    public void run(TestCase testCase) {
        JAU.toString(this);
    }

    public static void main(String[] params) {
        GGJAUPerfTest2 a = new GGJAUPerfTest2();
        for (int i = 0; i < 1000000; i++) {
            JAU.toString(a);
        }
    }
}