package com.googlecode.jau;

/**
 * A class with only one string field.
 */
public class BigString {
    public String value;

    public void setLength(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++)
            sb.append((char) ('0' + n % 10));
        this.value = sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BigString other = (BigString) obj;
        if ((this.value == null) ? (other.value != null)
                : !this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
