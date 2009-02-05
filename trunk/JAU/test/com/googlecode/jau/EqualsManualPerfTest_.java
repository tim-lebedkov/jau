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
        first = new RealClass();
        second = new RealClass();
    }

    @Override
    public void warmup(TestCase testCase) {
        result = first.equals(second);
    }

    public void run(TestCase testCase) {
        result = first.equals(second);
    }
}