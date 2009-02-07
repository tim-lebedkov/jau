package com.googlecode.jau;

/**
 * Test modelled after
 * http://java.dzone.com/tips/refactor-safe-tostringbuilder.
 */
@JAUToString
class PerfTest {
    public static void main(String[] params) {
        RealClass a = new RealClass();
        RealClass b = new RealClass();
        for (int i = 0; i < 10000000; i++) {
            //JAU.toString(a);
            JAU.equals(a, b);
        }
    }
}