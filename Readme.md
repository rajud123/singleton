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

What is serialVersionUID, why it is important to add in a Serilizable class.
It is important to declare a serialVersionId for he serializable class, if we don't specify the serialVersionId by manually then 
JVM will generate a serialVersionUID automatically at compile time. This generated value is based on the class's structure,
including its fields, methods, and superclass and  may vary depending on compiler implementations.

The serialization runtime associates the class a version number, called a serialVersionUID, which is used during deserialization 
to verify that the sender and receiver of a serialized object have loaded classes for that object that are compatible with respect
to serialization. If the receiver has loaded a class for the object that has a different serialVersionUID than that of the 
corresponding sender's class, then deserialization will result in an InvalidClassException. A serializable class can declare
its own serialVersionUID explicitly by declaring a field named serialVersionUID that must be static, final, and of type long:

ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;
