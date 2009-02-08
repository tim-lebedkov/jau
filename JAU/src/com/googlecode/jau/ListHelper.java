package com.googlecode.jau;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * hashcode()/equals() for a list
 */
class ListHelper<E, T extends List<E>> implements HashCoder<T>, Comparator<T> {
    /** an instance of this class. */
    public static final HashCoder INSTANCE = new ListHelper();

    @Override
    public int hashCode(T list) {
        int hashCode = 1;
        Iterator<E> i = list.iterator();
        while (i.hasNext()) {
            E obj = i.next();
            hashCode = 31*hashCode + (obj==null ? 0 : JAU.hashCode(obj));
        }
        return hashCode;
    }

    @Override
    public int compare(T o1, T o2) {
        if (o2 == o1)
            return 0;
        if (!(o2 instanceof List))
            return -1;

        ListIterator<E> e1 = o1.listIterator();
        ListIterator e2 = ((List) o2).listIterator();
        while(e1.hasNext() && e2.hasNext()) {
            E o1_ = e1.next();
            Object o2_ = e2.next();
            int r = JAU.compare(o1_, o2_);
            if (r != 0)
                return r;
        }
        if (e1.hasNext())
            return 1;
        else if (e2.hasNext())
            return -1;
        else
            return 0;
    }
}
