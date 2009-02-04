package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Testing performance of .equals()
 */
public class HashCodeJAUPerfTest_ extends JapexDriverBase {
    private static Object first;
    private static int result;

    @Override
    public void prepare(TestCase testCase) {
        first = new RealClass();
    }

    public void run(TestCase testCase) {
        result = JAU.hashCode(first);
    }
}