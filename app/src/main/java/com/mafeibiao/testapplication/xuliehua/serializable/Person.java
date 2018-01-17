package com.mafeibiao.testapplication.xuliehua.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by mafeibiao on 2018/1/2.
 */

public class Person extends PersonParent implements Serializable {
    private static final long serialVersionUID = 1L;

    //静态域
    public static int static_field;
    //transient域
    public transient int transient_field;
    //一个普通的域
    public String desc;

    public Person(String desc) {
        this.desc = desc;
    }

    static class PersonSerializableProxy implements Serializable{
        private String desc;

        private PersonSerializableProxy(Person s) {
            this.desc = s.desc;
        }

        /**
         * 与writeReplace相同，ObjectInputStream会通过反射调用 readResolve()这个方法，
         * 决定是否替换反序列化出来的对象。
         * @return
         */
        private Object readResolve() {
            return new Person(desc);
        }

    }

    /**
     *
     * 在序列化一个对象时，ObjectOutputStream会通过反射首先调用writeReplace这个方法，
     * 在这里我们可以替换真正送去序列的对象，
     * 如果我们没有重写，那序列化的对象就是最开始的对象。
     * @return
     */
    private Object writeReplace() {
         //序列化Person的时候我们并没有直接写入Person对象，而是写入了PersonSerializableProxy对象
        return new PersonSerializableProxy(this);
    }

    /**
     * 这里主要是为了防止攻击,任何以Person声明的对象字节流都是流氓!!
     * 因为我在writeReplace中已经把序列化的实例指向了SerializableProxy
     * @param stream
     * @throws InvalidObjectException
     */
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("proxy requied!");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person = new Person("desc");
        person.transient_field = 100;
        person.static_field = 10086;

        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("cache.txt"));
        outputStream.writeObject(person);
        outputStream.flush();
        outputStream.close();

        person.static_field = 10087;


        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("cache.txt"));
        Person deserialObj = (Person) objectInputStream.readObject();
        System.out.println(deserialObj);
    }


}
class PersonParent{
    private String name;
    //PersonParent类要么继承自Serializable，要么需要提供一个无参构造器。
    public PersonParent() {
    }

    public PersonParent(String name) {
        this.name = name;
    }
}