package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Test modelled after
 * http://java.dzone.com/tips/refactor-safe-tostringbuilder.
 */
public class GGManualPerfTest2 extends JapexDriverBase {
    private int x = 1;
    private int y = 2;
    private int z = 3;
    private double a = 5.4343;
    private String txt = "Testme";

    public void run(TestCase testCase) {
        String s = super.toString() +
                "(x="+x + ", y="+ y + ", z="+z + ", a="+a+", txt=\""+txt+"\")";
    }
}