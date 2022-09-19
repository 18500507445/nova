package com.nova.design.factory.abstr;


import com.nova.design.factory.BluetoothSender;
import com.nova.design.factory.Sender;
import com.nova.design.factory.WiFiSender;

/**
 * Created by Landy on 2018/8/20.
 */
public class SenderFactory extends AbstractFactory {

    @Override
    public Sender createBluetoothSender() {
        return new BluetoothSender();
    }

    @Override
    public Sender createWifiSender() {
        return new WiFiSender();
    }
}
