package com.company;

import com.bluetooth.*;

import java.util.List;

public class BlueToothStuff {


    BluetoothAdapter bluetoothAdapter = new BluetoothAdapter();
    List<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getDevices();
    CeilingLight cl = null;
    StandingLight sl = null;
    Fan fan = null;


    public void blueTooth() {
        for (
                BluetoothDevice bluetoothDevice : bluetoothDevices) {
            if (bluetoothDevice.getType().equals("C_LAMP")) {
                cl = (CeilingLight) bluetoothDevice;
            } else if (bluetoothDevice.getType().equals("S_LAMP")) {
                sl = (StandingLight) bluetoothDevice;
            } else if (bluetoothDevice.getType().equals("FAN")) {
                fan = (Fan) bluetoothDevice;
            }
        }
    }
public void turnOnLight(){
        cl.turnOnSet("1111");
}
}









