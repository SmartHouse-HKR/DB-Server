package com.company;
import com.bluetooth.*;
import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;

public class SubscribeCallback implements MqttCallbackExtended {

    private ArrayList<Integer> voltageOverview;
    private DatabaseConnection db;
    CeilingLight cl = null;
    StandingLight sl = null;
    Fan fan = null;
    public SubscribeCallback() {
        db = new DatabaseConnection();
        voltageOverview = new ArrayList<>();
    }

    @Override
    public void connectComplete(boolean arg0, String arg1){
        BluetoothAdapter bluetoothAdapter = new BluetoothAdapter();
        List<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getDevices();


        for (BluetoothDevice bluetoothDevice : bluetoothDevices) {
            if (bluetoothDevice.getType().equals("C_LAMP")) {
                cl = (CeilingLight) bluetoothDevice;
            } else if (bluetoothDevice.getType().equals("S_LAMP")) {
                sl = (StandingLight) bluetoothDevice;
            } else if (bluetoothDevice.getType().equals("FAN")) {
                fan = (Fan) bluetoothDevice;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        byte[] mqttmessage = message.getPayload();
        String mqttmessageString = new String (mqttmessage);
        db.updateMessages(topic, mqttmessageString);

        /*if(topic.equals("smarthouse/voltage/value")){
            int voltage = Integer.parseInt(mqttmessageString);
            db.updateVoltage(voltage);
            voltageOverview.add(voltage);
        }*/

        switch (topic) {
            case "smarthouse/indoor_light/state":

                break;
            case "smarthouse/outdoor_light/state":
                System.out.println(2);
                break;
            case "smarthouse/heater_1/state":
                System.out.println(3);
                break;
            case "smarthouse/heater_2/state":
                System.out.println(4);
                break;
            case "smarthouse/heater_1/value":
                System.out.println(5);
                break;
            case "smarthouse/heater_2/value":
                System.out.println(6);
                break;
            case "smarthouse/fan/speed":
                System.out.println(7);
                break;
            case "smarthouse/outdoor_temperature/value":
                System.out.println(8);
                break;
            case "smarthouse/indoor_temperature/value":
                System.out.println(9);
                break;
            case "smarthouse/voltage/value":
                System.out.println(10);
                break;
            case "smarthouse/voltage/overview":
                System.out.println(11);
                break;
            case "smarthouse/power/state":
                System.out.println(12);
                break;
            case "smarthouse/burglar_alarm/state":
                System.out.println(13);
                break;
            case "smarthouse/burglar_alarm/trigger":
                System.out.println(14);
                break;
            case "smarthouse/fire_alarm/state":
                System.out.println(15);
                break;
            case "smarthouse/fire_alarm/trigger":
                System.out.println(16);
                break;
            case "smarthouse/water_leak/trigger":
                System.out.println(17);
                break;
            case "smarthouse/oven/state":
                System.out.println(18);
                break;
            case "smarthouse/window_alarm/state":
                System.out.println(19);
                break;
            case "smarthouse/window_alarm/trigger":
                System.out.println(20);
                break;
            case "smarthouse/microwave/manual_start":
                System.out.println(21);
                break;
            case "smarthouse/microwave/preset_start":
                System.out.println(22);
                break;
            case "smarthouse/microwave/error":
                System.out.println(23);
                break;
            case "smarthouse/bt_fan/state":
                if(mqttmessageString.equals("on"))
                {
                    System.out.println("turning fan on");
                    fan.turnOnFan();
                }
                else {
                    System.out.println("turning fan off");
                    fan.turnOffFan();
                }
                break;
            case "smarthouse/bt_fan/swing":
                fan.changeSwing();
                System.out.println("swinging fan");
                break;
            case "smarthouse/bt_fan/speed":
                if(mqttmessageString.equals("higher")){
                    fan.speedUp();
                    System.out.println("fan up");
                }else{
                    fan.speedDown();
                    System.out.println("fan down");
                }
                break;
            case "smarthouse/bt_light/state":
                if(mqttmessageString.equals("1111")){
                   cl.turnOnSet("1111");
                }else{
                    cl.turnOnSet("0001");
                }
                break;
            case "smarthouse/bt_lamp/state":
                if(mqttmessageString.equals("on")){
                    sl.turnOnLamp();
                }else{
                    sl.turnOffLamp();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("I AM A CONNECTION AND I AM LOST!!");
    }
}

