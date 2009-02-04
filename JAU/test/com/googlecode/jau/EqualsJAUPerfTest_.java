package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Testing performance of .equals()
 */
public class EqualsJAUPerfTest_ extends JapexDriverBase {
    private int x = 1;
    private int y = 2;
    private int z = 3;
    private double a = 5.4343;
    private String txt = "Testme";

    private static Object second;
    private static boolean result;

    @Override
    public void prepare(TestCase testCase) {
        second = new EqualsJAUPerfTest_();
    }

    public void run(TestCase testCase) {
        result = JAU.equals(this, second);
    }
}