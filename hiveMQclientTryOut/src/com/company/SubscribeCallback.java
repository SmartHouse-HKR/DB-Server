package com.company;

import org.eclipse.paho.client.mqttv3.*;

import java.sql.DriverManager;
import java.sql.SQLException;

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
        db.updateDB(topic, mqttmessageString);
    }

    @Override
    public void connectionLost(Throwable cause) {

    }
}

