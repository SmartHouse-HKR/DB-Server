package com.company;


import com.bluetooth.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String topic = "smarthouse/bt_light1/state";
        String content = "1111";
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        char[] pass = new char[2];
        String testMessage;

        BluetoothAdapter bluetoothAdapter = new BluetoothAdapter();
        List<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getDevices();
        CeilingLight cl = null;
        StandingLight sl = null;
        Fan fan = null;
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
        try {
            pass[0] = 'a';
            pass[1] = 'b';
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("admin-user");
            connOpts.setPassword(pass);

            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
         //  sampleClient.subscribe("smarthouse/bt_fan1/state");

            sampleClient.publish(topic, message);
            System.out.println("Message published");

            CountDownLatch receivedSignal = new CountDownLatch(10);
            MqttClient subscriber = new MqttClient(broker, clientId, persistence);
            subscriber.setCallback(new SubscribeCallback());
            MqttConnections mqttConnections = new MqttConnections();
            mqttConnections.MakeAconnect();

      do {

          System.out.println("Please input the message you would like to send to the device");
                testMessage = scanner.nextLine();
                System.out.println("please input topic name");
                topic = scanner.nextLine();
              //  sampleClient.subscribe("smarthouse/indoor_light/state");
          mqttConnections.publishMqttMessage(topic, testMessage);

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


