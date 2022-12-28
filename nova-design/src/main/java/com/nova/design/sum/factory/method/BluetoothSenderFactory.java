package com.nova.design.sum.factory.method;


import com.nova.design.sum.factory.BluetoothSender;
import com.nova.design.sum.factory.Sender;

/**
 * @author landyl
 * @create 4:37 PM 05/12/2018
 */
public class BluetoothSenderFactory implements SenderFactory {
    @Override
    public Sender createSender() {
        return new BluetoothSender();
    }
}
