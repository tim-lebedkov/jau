**java.lang.Boolean (similar implementations for java.lang.Byte, java.lang.Character,
java.lang.Integer, java.lang.Long, java.lang.Short)**

implementable using JAU: yes
```
    private final boolean value;

    public boolean equals(Object obj) {
	if (obj instanceof Boolean) {
	    return value == ((Boolean)obj).booleanValue();
	} 
	return false;
    }
```

**java.lang.Character.Subset (similar implementation for java.lang.Enum)**

implementable using JAU: no
```
    private String name;

    public final boolean equals(Object obj) {
        return (this == obj);
    }
```

**java.lang.Double (similar implementation for java.lang.Float)**

implementable using JAU: no
```
    public boolean equals(Object obj) {
	return (obj instanceof Double)
	       && (doubleToLongBits(((Double)obj).value) ==
		      doubleToLongBits(value));
    }
```

**java.lang.StackTraceElement**

implementable using JAU: yes
```
    public boolean equals(Object obj) {
        if (obj==this)
            return true;
        if (!(obj instanceof StackTraceElement))
            return false;
        StackTraceElement e = (StackTraceElement)obj;
        return e.declaringClass.equals(declaringClass) && e.lineNumber == lineNumber
            && eq(methodName, e.methodName) && eq(fileName, e.fileName);
    }
```

**java.lang.String**

implementable using JAU: no
```
    public boolean equals(Object anObject) {
	if (this == anObject) {
	    return true;
	}
	if (anObject instanceof String) {
	    String anotherString = (String)anObject;
	    int n = count;
	    if (n == anotherString.count) {
		char v1[] = value;
		char v2[] = anotherString.value;
		int i = offset;
		int j = anotherString.offset;
		while (n-- != 0) {
		    if (v1[i++] != v2[j++])
			return false;
		}
		return true;
	    }
	}
	return false;
    }
```

**java.lang.Constructor**

implementable using JAU: no
```
    public boolean equals(Object obj) {
	if (obj != null && obj instanceof Constructor) {
	    Constructor other = (Constructor)obj;
	    if (getDeclaringClass() == other.getDeclaringClass()) {
		/* Avoid unnecessary cloning */
		Class[] params1 = parameterTypes;
		Class[] params2 = other.parameterTypes;
		if (params1.length == params2.length) {
		    for (int i = 0; i < params1.length; i++) {
			if (params1[i] != params2[i])
			    return false;
		    }
		    return true;
		}
	    }
	}
	return false;
    }
```

**java.lang.Field**

implementable using JAU: no
```
    public boolean equals(Object obj) {
	if (obj != null && obj instanceof Field) {
	    Field other = (Field)obj;
	    return (getDeclaringClass() == other.getDeclaringClass())
                && (getName() == other.getName())
		&& (getType() == other.getType());
	}
	return false;
    }
```

**java.lang.Method**

implementable using JAU: no
```
    public boolean equals(Object obj) {
	if (obj != null && obj instanceof Method) {
	    Method other = (Method)obj;
	    if ((getDeclaringClass() == other.getDeclaringClass())
		&& (getName() == other.getName())) {
		if (!returnType.equals(other.getReturnType()))
		    return false;
		/* Avoid unnecessary cloning */
		Class[] params1 = parameterTypes;
		Class[] params2 = other.parameterTypes;
		if (params1.length == params2.length) {
		    for (int i = 0; i < params1.length; i++) {
			if (params1[i] != params2[i])
			    return false;
		    }
		    return true;
		}
	    }
	}
	return false;
    }
```