package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Testing performance of .equals()
 */
public class EqualsManualStringPerfTest_ extends JapexDriverBase {
    private static BigString first, second;

    @Override
    public void prepare(TestCase testCase) {
        int n = testCase.getIntParam("n");
        first = new BigString();
        first.setLength(n);
        second = new BigString();
        second.value = new String(first.value.toCharArray());
    }

    private static boolean result;

    public void run(TestCase testCase) {
        result = first.equals(second);
    }
}