package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Testing performance of .equals()
 */
public class EqualsManualPerfTest_ extends JapexDriverBase {
    private static Object first, second;
    private static boolean result;

    @Override
    public void prepare(TestCase testCase) {
        first = new EqualsManualPerfTest_();
        second = new EqualsManualPerfTest_();
    }

    public void run(TestCase testCase) {
        result = first.equals(second);
    }
}