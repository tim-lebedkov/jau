package com.googlecode.jau;

/**
 * "Real life example"
 */
@JAUEquals
@JAUHashCode
class RealClass {
    private int x = 1;
    private int y = 2;
    private int z = 3;
    private double a = 5.4343;
    private String txt = "Testme";

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RealClass other = (RealClass) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        if (Double.doubleToLongBits(this.a) != Double.doubleToLongBits(other.a)) {
            return false;
        }
        if ((this.txt == null) ? (other.txt != null) : !this.txt.equals(other.txt)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.x;
        hash = 23 * hash + this.y;
        hash = 23 * hash + this.z;
        hash =
                23 * hash +
                (int) (Double.doubleToLongBits(this.a) ^
                (Double.doubleToLongBits(this.a) >>> 32));
        hash = 23 * hash + (this.txt != null ? this.txt.hashCode() : 0);
        return hash;
    }
}
