package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Test modelled after
 * http://java.dzone.com/tips/refactor-safe-tostringbuilder.
 */
@JAUToString
public class GGJAUPerfTest_ extends JapexDriverBase {
    private static RealClass v = new RealClass();

    public void run(TestCase testCase) {
        for (int i = 0; i < 1000; i++)
            JAU.toString(v);
    }

    public static void main(String[] params) {
        PerfTest a = new PerfTest();
        for (int i = 0; i < 1000000; i++) {
            JAU.toString(a);
        }
    }
}
