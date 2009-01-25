package com.googlecode.jau;

@JAUEquals
@JAUToMap
@JAUToString(type=JAUToStringType.MANY_LINES)
public class ClassOne {
    public byte byte_ = 34;
    public short short_ = 324;
    public int int_ = 1233;
    public long long_ = 2133333L;
    public float float_ = 3234.22f;
    public double double_ = 1232.123423;
    public String string_ = "asdfjksjd";

    public byte[] bytea = new byte[] {1, 2, 3, 4};
    public short[] shorta = new short[] {23, 423, 23, 33};
    public int[] inta = new int[] {23323, 4423, 231, -1, -23323};
    public long[] longa = new long[] {23423432L, 23434324L, 49887L, -123434213L};
    public float[] floata = new float[] {3.3f, 543423.3f, -1232.22222f};
    public double[] doublea = new double[] {233.243, 5098.23094874, -37284.540861};
    public String[] stringa = new String[] {"sdfsadfas", "owu-ycxvcnowefi",
            "wopeiurpg., aas dflaks df#pwe"};

    public byte[][] bytea2 = new byte[][] {{2, 4, 21, 5}, {-11}, {}, {57, 38, 8}};
    public short[][] shorta2 = new short[][] {
        {2, 21341, 333, -1234, 580},
        {457, 3847},
        {},
        {-123}, {329, 2348, 4538, 4019, 5481},
        {-839, -401}
    };
    public int[][] inta2 = new int[][] {};
    public long[][] longa2 = new long[][] {
        {39788, -9128347650L, 2389459},
        {},
        {-23450987, -4359087},
        {4587458, 3, 255098},
        {9045603L, 45098754L, 787573265344L, 435989647},
        {4543985734059834L, 28597589234L, -23947923487L}
    };
    public float[][] floata2 = new float[][] {
        {349857.349578f, 60287.6598745f},
        {},
        {-345987.435897f},
    };
    public double[][] doublea2 = new double[][] {
        {289347892374.48378347},
        {},
        {234978234.3, 96798743.234, -469874395.44444},
        {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY},
        {Double.MAX_VALUE, Double.NaN},
    };
    public String[][] stringa2 = new String[][] {
        {null},
        {"asldfjk sal"},
        {},
        {"lkj9324j", "5sfgjfsdjkl"}
    };

    public void clear() {
        byte_ = 0;
        short_ = 0;
        int_ = 0;
        long_ = 0;
        float_ = 0;
        double_ = 0;
        string_ = null;

        bytea = null;
        shorta = null;
        inta = null;
        longa = null;
        floata = null;
        doublea = null;
        stringa = null;

        bytea2 = null;
        shorta2 = null;
        inta2 = null;
        longa2 = null;
        floata2 = null;
        doublea2 = null;
        stringa2 = null;
    }
}
