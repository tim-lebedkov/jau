package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Testing performance of .equals()
 */
public class EqualsJAUIntPerfTest_ extends JapexDriverBase {
    private static Object first, second;

    @Override
    public void prepare(TestCase testCase) {
        int n = testCase.getIntParam("n");
        switch (n) {
            case 1:
                first = new IntFields1();
                second = new IntFields1();
                break;
            case 2:
                first = new IntFields2();
                second = new IntFields2();
                break;
            case 4:
                first = new IntFields4();
                second = new IntFields4();
                break;
            case 8:
                first = new IntFields8();
                second = new IntFields8();
                break;
            case 16:
                first = new IntFields16();
                second = new IntFields16();
                break;
            case 32:
                first = new IntFields32();
                second = new IntFields32();
                break;
        }
        System.out.println(first.getClass() + " " + second.getClass());
    }

    private static boolean result;

    public void run(TestCase testCase) {
        result = JAU.equals(first, second);
    }
}