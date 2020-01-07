package com.company;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


public class Main {

    public static void main(String[] args) {
        try{
            MqttConnections mqttConnections = new MqttConnections();
            mqttConnections.MakeAconnect();
        } catch(MqttException e){
            e.printStackTrace();
        }
    }
}


