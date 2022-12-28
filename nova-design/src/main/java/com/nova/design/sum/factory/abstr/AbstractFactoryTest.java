package com.nova.design.sum.factory.abstr;


import com.nova.design.sum.factory.Sender;

/**
 * 在实际应用中最为广泛
 * Created by Landy on 2018/8/20.
 */
public class AbstractFactoryTest {

    public static void main(String[] args) {
        SenderFactory senderFactory = new SenderFactory();
        Sender sender = senderFactory.createBluetoothSender();
        byte[] bytes = new byte[3];
        sender.sendData(bytes);
    }

}
