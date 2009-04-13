package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Test modelled after
 * http://java.dzone.com/tips/refactor-safe-tostringbuilder.
 */
public class GGManualPerfTest_ extends JapexDriverBase {
    private static RealClass v = new RealClass();

    public void run(TestCase testCase) {
        v.toString();
    }
}