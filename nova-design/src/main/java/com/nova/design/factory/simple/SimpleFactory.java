package com.nova.design.factory.simple;


import com.nova.design.factory.BluetoothSender;
import com.nova.design.factory.Sender;
import com.nova.design.factory.WiFiSender;

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
