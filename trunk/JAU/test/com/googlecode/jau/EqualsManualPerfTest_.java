package com.googlecode.jau;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 * Testing performance of .equals()
 */
public class EqualsManualPerfTest_ extends JapexDriverBase {
    private int x = 1;
    private int y = 2;
    private int z = 3;
    private double a = 5.4343;
    private String txt = "Testme";

    private static Object second;
    private static boolean result;

    @Override
    public void prepare(TestCase testCase) {
        second = new EqualsManualPerfTest_();
    }

    public void run(TestCase testCase) {
        EqualsManualPerfTest_ second_;
        if (!(second instanceof EqualsManualPerfTest_))
            result = false;
        else {
            second_ = (EqualsManualPerfTest_) second;
            result = (this.x == second_.x) && (this.y == second_.y) &&
                    (this.z == second_.z) &&
                    (Double.compare(this.a, second_.a) == 0) &&
                    (this.txt.equals(second_.txt));
        }
    }
}