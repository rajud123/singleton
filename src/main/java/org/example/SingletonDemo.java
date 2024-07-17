package org.example;

import java.io.*;
import java.lang.Class;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SingletonDemo implements Serializable, Cloneable {


    private static volatile SingletonDemo INSTANCE;

    static final long serialVersionUID = 42L;
    //when clone() is called on this object then it will throw clone CloneNotSupportedException
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    //called interally by readObject() duriing deSerailization
    public Object readResolve() {
        throw new IllegalArgumentException("Deserialization is not supported on this class");
    }

    //making the constructor private so we can not create the object outisde the class
    private SingletonDemo() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Can't instantiate singleton twice");
        }
    }

    public static SingletonDemo getInstance() {

        if (INSTANCE == null) {
            synchronized (SingletonDemo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SingletonDemo();
                }
            }
        }
        return INSTANCE;
    }

    public static void main(String[] args) {

        SingletonDemo instance = getInstance();
       /* try {
            Object clone = instance.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }*/


        //serliazing object
        /*try {
            FileOutputStream fileOutputStream = new FileOutputStream("data.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(instance);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //deserilizing object
        try {
            FileInputStream fileInputStream = new FileInputStream("data.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            SingletonDemo singletonDemo = (SingletonDemo)objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/



        //using reflection api to break singleton pattern
        /*Class<SingletonDemo> singletonDemoClass = SingletonDemo.class;
        Constructor<?>[] declaredConstructors = singletonDemoClass.getDeclaredConstructors();


        //making the private constructor accessible outside the class
       declaredConstructors[0].setAccessible(true);

        try {
            SingletonDemo instance1 = (SingletonDemo)declaredConstructors[0].newInstance();
            SingletonDemo instance2 = (SingletonDemo)declaredConstructors[0].newInstance();
            SingletonDemo instance3 = (SingletonDemo)declaredConstructors[0].newInstance();
            System.out.println("instance1 "+instance1.hashCode());
            System.out.println("instance1 "+instance2.hashCode());
            System.out.println("instance1 "+instance3.hashCode());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }*/

    }





}

class SingletonThread implements Runnable{


    @Override
    public void run() {

        SingletonDemo instance = SingletonDemo.getInstance();
        System.out.println("hash code "+instance.hashCode());
    }
}

