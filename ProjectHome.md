**THIS PROJECT IS NOT BEING DEVELOPED ANYMORE.**
**1.0 is the first and the last release available**

This library will help you to implement common methods like .equals() of .hashCode() using annotations.

See also the discussion on JavaLobby: http://java.dzone.com/announcements/jau-java-annotation-based-util

Example of usage:
```
import com.googlecode.jau.*;

@JAUEquals
@JAUHashCode
@JAUToString
@JAUCompare
@JAUCopy
@JAUToMap
public class UserData implements Comparable, Cloneable {
    private String firstName;
    private String secondName;
    private Date birthDate;
    private String login;
     
    @JAUEquals // not really necessary. All fields are included by default.
    private String[] rights = new String[] {"view"};
 
    @JAUEquals(include=false) // ignore listeners
    @JAUHashCode(include=false)
    @JAUCompare(include=false)
    private javax.swing.event.EventListenerList listeners = new javax.swing.event.EventListenerList();
 
    // constructor omitted 

    public boolean equals(Object obj) {
        return JAU.equals(this, obj);
    } 

    public int hashCode() {
        return JAU.hashCode(this);
    }

    public String toString() {
        return JAU.toString(this);
    }

    public int compareTo(Object obj) {
        return JAU.compare(this, obj);
    }

    // deep(!) copy
    public UserData clone() throws CloneNotSupportedException {
        UserData r = (UserData) super.clone(this);
        JAU.copy(this, r);
        return r;
    }
}
```

For example, the output for
```
JAU.toString(new String[][] {{"a", "b"}, {"1", "2"}})
```
would be
<pre>
java.lang.String[][java.lang.String["a", "b"], java.lang.String["1", "2"]]<br>
</pre>

see http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/EqualsTest.java, http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/ToStringTest.java and
http://code.google.com/p/jau/source/browse/trunk/JAU/test/com/googlecode/jau/CompareTest.java for more test cases
