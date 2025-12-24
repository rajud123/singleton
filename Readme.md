The java class that allow us to create only one object in any given situation is called Singleton class,

We can take class as singleton, when the class does not contain any state(data variables), or read only state(final variables),
or sharable state(for cache) across the application.

There are many ways we can break a singleton class to create multiple instances
1.we can use cloning or deserialize an object to create a new object, in this case constructor wont be executed, but it does
not mean there is no new object created.

to avoid cloning or deserilization of object
 we can override these methods in our class to avoid creating new object.

    @Override
    protected Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
    }

    //called interally by readObject() method of ObjectInputStream during deSerailization
    public Object readResolve() {
        throw new IllegalArgumentException("Deserialization is not supported on this class");
    }
2. There is also chance that if multiple threads try to access on the class, and due to thread switching multiple objects get created,
to avoid creating multiple objects, we can make use of synchronized block, so only one thread is allow to act on the class.

3.There can be also cases,using Reflection API we can use the private constructor of java and can have access to it even though the 
constructor is private, so tp avoid this we can add a condition in constructor, checking if the object has already been created or not,
if the object already present then we throw Exception.

------------------------------------------------------

You might be thinking of not making the class implementing the Serializable and Cloneable interface but there can be cases, where not your class but the class you are extending can implement these interfaces, so in that case your class will indirectly implement Cloneable and Serializable interface, so you need to take care of not breaking the situation.

What is serialVersionUID, why it is important to add in a Serilizable class.

In Java, when a class implements the `Serializable` interface, it allows instances of that class to be converted into a byte stream (serialization) for storage or transmission, and later reconstructed (deserialization). The `serialVersionUID` is a unique identifier (a `long` value) associated with the class to ensure version compatibility during this process.

### Why Use serialVersionUID?
Without an explicit `serialVersionUID`, the Java runtime automatically computes one based on the class's structure, including its fields, methods, interfaces, and other details. This default UID is sensitive to even minor changes in the class (e.g., adding or removing a field, changing a method signature). If the class evolves after an object has been serialized, the computed UID will differ, leading to an `InvalidClassException` during deserialization because the system detects a mismatch.

By declaring a `private static final long serialVersionUID` explicitly in the class, you take control of the versioning. This allows you to decide when a change is "compatible" (keep the same UID, so old serialized objects can still be deserialized, with new fields defaulting to null or zero) versus "incompatible" (change the UID to force an exception and prevent data corruption). It's particularly important in distributed systems, long-term storage, or when classes are updated over time, as it prevents subtle bugs from incompatible class versions.

### Example
Consider a simple `Person` class that implements `Serializable`:

```java
import java.io.Serializable;

public class Person implements Serializable {
    // No explicit serialVersionUID declared here
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

You serialize an instance of this class to a file (e.g., using `ObjectOutputStream`). Later, you modify the class by adding a new field:

```java
import java.io.Serializable;

public class Person implements Serializable {
    // Still no explicit serialVersionUID
    private String name;
    private int age;  // New field added

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```

Attempting to deserialize the old serialized object will throw an `InvalidClassException` because the auto-generated UID has changed due to the added field.

To fix this and maintain compatibility, add an explicit `serialVersionUID` from the start (you can generate one using the `serialver` tool or just assign a value like `1L`):

```java
import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;  // Explicit UID
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

After adding the `age` field, keep the same UID if the change is compatible:

```java
import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;  // Same UID
    private String name;
    private int age;  // New field

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```

Now, deserializing the old object succeeds—the `age` field will default to 0 (its primitive default). If the change were incompatible (e.g., removing the `name` field), you'd increment the UID to `2L` to trigger an exception and avoid data issues.
