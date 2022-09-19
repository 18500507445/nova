package com.nova.design.factory.method;


import com.nova.design.factory.BluetoothSender;
import com.nova.design.factory.Sender;

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
