package com.nova.design.sum.factory.simple;


import com.nova.design.sum.factory.BluetoothSender;
import com.nova.design.sum.factory.Sender;
import com.nova.design.sum.factory.WiFiSender;

/**
 * @author landyl
 * @create 4:33 PM 05/12/2018
 */
public class SimpleFactory {

    public static Sender createSender(String mode) {
        switch (mode) {
            case "Wi-Fi":
                return new WiFiSender();
            case "Bluetooth":
                return new BluetoothSender();
            default:
                throw new IllegalArgumentException("illegal type: " + mode);
        }
    }

}
