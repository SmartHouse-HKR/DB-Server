package com.company;

import org.eclipse.paho.client.mqttv3.*;

public class SubscribeCallback implements MqttCallback {
    private DatabaseConnection db;

    public SubscribeCallback() {
        db = new DatabaseConnection();

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        byte[] mqttmessage = message.getPayload();
        String mqttmessageString = new String (mqttmessage);
        db.updateMessages(topic, mqttmessageString);

        switch (topic) {
            case "smarthouse/indoor_light/state":
                System.out.println(1);
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
                System.out.println(24);
                break;
            case "smarthouse/bt_fan/swing":
                System.out.println(25);
                break;
            case "smarthouse/bt_fan/speed":
                System.out.println(26);
                break;
            case "smarthouse/bt_light/state":
                System.out.println(27);
                break;
            case "smarthouse/bt_lamp/state":
                System.out.println(28);
                break;
            default:
                break;
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }
}

