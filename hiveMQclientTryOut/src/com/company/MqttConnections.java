package com.company;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttConnections {
    String topic;

    int qos;
    String broker;
    String clientID;
    MemoryPersistence persistence;
    char[] password = new char[2];
    MqttClient mqttClient;
    MqttMessage message;


    public void MakeAconnect() throws MqttException {
        password[0] = 'a';
        password[1] = 'b';
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setPassword(password);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName("admin-user");
        broker = "tcp://localhost:1883";
        qos = 2;
        clientID = "JavaSample";
        persistence = new MemoryPersistence();
        mqttClient = new MqttClient(broker, clientID, persistence);
        mqttClient.connect(mqttConnectOptions);

    }




    }

