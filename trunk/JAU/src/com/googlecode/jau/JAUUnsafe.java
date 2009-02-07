package com.googlecode.jau;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * Implementation of JAU methods using sun.misc.Unsafe.
 */
class JAUUnsafe implements JAUInterface {
    private Unsafe UNSAFE;

    /**
     * -
     * 
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.IllegalAccessException
     */
    public JAUUnsafe() throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        UNSAFE = (Unsafe)field.get(null);
    }

    public void update(ClassInfo ci) {
        for (int i = 0; i < ci.fields.length; i++)
            ci.offsets[i] = UNSAFE.objectFieldOffset(ci.fields[i]);
    }

    @Override
    public boolean equals(ClassInfo ci, Object a, Object b) {
        for (int i = 0; i< ci.offsets.length; i++) {
            long offset = ci.offsets[i];
            switch (ci.types[i]) {
                case ClassInfo.INTEGER_TYPE:
                    if (UNSAFE.getInt(a, offset) != UNSAFE.getInt(b, offset))
                        return false;
                    else
                        break;
                case ClassInfo.BYTE_TYPE:
                    if (UNSAFE.getByte(a, offset) != UNSAFE.getByte(b, offset))
                        return false;
                    else
                        break;
                case ClassInfo.SHORT_TYPE:
                    if (UNSAFE.getShort(a, offset) != UNSAFE.getShort(b, offset))
                        return false;
                    else
                        break;
                case ClassInfo.LONG_TYPE:
                    if (UNSAFE.getLong(a, offset) != UNSAFE.getLong(b, offset))
                        return false;
                    else
                        break;
                case ClassInfo.FLOAT_TYPE:
                    if (Float.floatToIntBits(UNSAFE.getFloat(a, offset)) !=
                            Float.floatToIntBits(UNSAFE.getFloat(b, offset)))
                        return false;
                    else
                        break;
                case ClassInfo.DOUBLE_TYPE:
                    if (Double.doubleToLongBits(UNSAFE.getDouble(a, offset)) !=
                            Double.doubleToLongBits(UNSAFE.getDouble(b, offset)))
                        return false;
                    else
                        break;
                case ClassInfo.CHARACTER_TYPE:
                    if (UNSAFE.getChar(a, offset) != UNSAFE.getChar(b, offset))
                        return false;
                    else
                        break;
                default:
                    if (!JAU.equals(UNSAFE.getObject(a, offset),
                            UNSAFE.getObject(b, offset)))
                        return false;
            }
        }
        return true;
    }
}
