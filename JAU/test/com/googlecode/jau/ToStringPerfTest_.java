package com.googlecode.jau;

import com.sun.japex.*;

/**
 * Performance tests.
 */
public class ToStringPerfTest_ extends JapexDriverBase {
    private int[] object;

    @Override
    public void prepare(TestCase testCase) {
        object = new int[1024];
        for (int i = 0; i < object.length; i++) {
            object[i] = i;
        }
    }

    public void run(TestCase testCase) {
        String s = JAU.toString(object);
    }
}
