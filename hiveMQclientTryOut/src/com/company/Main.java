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
        Scanner scanner = new Scanner(System.in);
        String topic = "MQTTExamples";
        String content = "Message from MqttPublishSampljkhjkfasdfasdffdahe db";
     // this a commment man
       int qos = 2;
        String brokerTest = "tcp://localhost:1883666666666";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        char[] pass = new char[2];
        String testMessage;
        try {
            pass[0] = 'a';
            pass[1] = 'b';
            MqttClient sampleClient = new MqttClient(brokerTest, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("admin-user");
            connOpts.setPassword(pass);

            System.out.println("Connecting to broker: " + brokerTest);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");

            CountDownLatch receivedSignal = new CountDownLatch(10);
            MqttClient subscriber = new MqttClient(brokerTest, clientId, persistence);
            subscriber.setCallback(new SubscribeCallback());

            MqttConnections mqttConnections = new MqttConnections();
            mqttConnections.MakeAconnect();
            do {
                System.out.println("Please input the message you would like to send to the device");
                testMessage = scanner.nextLine();
                mqttConnections.publishMqttMessage("MQTTExamples", testMessage);
            } while (!testMessage.equals("Exit"));


        } catch (MqttException me) {
            System.out.println("reason ");
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}


