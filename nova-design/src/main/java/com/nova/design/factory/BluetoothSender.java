package com.nova.design.factory;


/**
 * @author landyl
 * @create 4:33 PM 05/12/2018
 */
public class BluetoothSender implements Sender {

    @Override
    public void sendData(byte[] data) {
        System.out.println("Send data by Bluetooth");
    }
}
