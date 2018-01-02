package com.mafeibiao.testapplication.xuliehua.serializable;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by mafeibiao on 2018/1/2.
 */

class Person implements Serializable {

    private String desc;

    public Person(String desc) {
        this.desc = desc;
    }

    static class SerializableProxy implements Serializable{
        private String desc;

        private SerializableProxy(Person s) {
            this.desc = s.desc;
        }

        /**
         * 在这里恢复外围类
         * 注意看这里!!!最大的好处就是我们最后得到的外围类是通过构造器构建的!
         * @return
         */
        private Object readResolve() {
            return new Person(desc);
        }

    }

    /**
     * 外围类直接替换成静态内部代理类作为真正的序列化对象
     * @return
     */
    private Object writeReplace() {
        return new SerializableProxy(this);
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
}
