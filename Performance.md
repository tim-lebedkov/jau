# Introduction #

Currently the library uses Java reflection API. That's why the current implementation is slower than implementing e.g. equals() manually. On the other hand the performance difference may be acceptable in particular cases. Additionally, nothing in the JAU API prevents an implementation using code generation or other techniques.

# Details #

Performance reports:

equals() for RealClass:
  * Class: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/RealClass.java
  * Test: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/EqualsJAUPerfTest_.java
  * Results: http://jau.googlecode.com/svn/trunk/JAU/reports/equals/index.html

equals() for a class with one String field (different string lengths):
  * Class: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/BigString.java
  * Test: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/EqualsJAUStringPerfTest_.java
  * Results: http://jau.googlecode.com/svn/trunk/JAU/reports/equalsBigString/index.html

equals() for classes with different number of int fields:
  * Class: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/IntFields32.java
  * Test: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/EqualsJAUIntPerfTest_.java
  * Results: http://jau.googlecode.com/svn/trunk/JAU/reports/equalsInt/index.html

hashCode() for RealClass:
  * Class: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/RealClass.java
  * Test: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/HashCodeJAUPerfTest_.java
  * Results: http://jau.googlecode.com/svn/trunk/JAU/reports/hashCode/index.html

toString() for RealClass:
  * Class: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/RealClass.java
  * Test: http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/ToStringPerfTest_.java
  * Results: http://jau.googlecode.com/svn/trunk/JAU/reports/toString/index.html

# Conclusions #
  * for "small" field types (byte, int, float) performance of equals()/hashCode() implemented with JAU depends heavily on the number of fields and is much worse than in manually implemented methods
  * for an object with one String field the value of the field must be of length 500 of higher for JAU overhead to become neglectable
  * toString() performance of JAU is really good even for classes with many small fields