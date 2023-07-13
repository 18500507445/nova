package com.nova.socket.codec;

import java.io.*;

public class ObjectSerializer implements Serializer{

    /**
     * 利用java IO 对象 -> 字节数组
     * @param obj
     * @return
     */
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }


    /**
     * 字节数组 -> 对象
     * @param bytes
     * @param messageType
     * @return
     */
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 0 代表java原生序列化器
     * @return
     */
    @Override
    public int getType() {
        return 0;
    }
}

