package com.company;

//import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSubscribe;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Timestamp;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String topic        = "MQTTExamples";
        String content      = "Message from MqttPublishSampljkhjkfasdfasdffdahe db";
        int qos             = 2;
        String broker       = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        char[] pass = new char[2];
       String testMessage;
        try {
           pass[0] = 'a';
           pass[1] ='b';
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
          connOpts.setUserName("admin-user");
          connOpts.setPassword(pass);

            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
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
             mqttConnections.publishMqttMessage("MQTTExamples", testMessage);
         }while (!testMessage.equals("Exit"));





        } catch(MqttException me) {
            System.out.println("reason ");
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

   /* public static void main(String[] args) throws MqttException {
        Main myapp = new Main();
        myapp.connectToServer();
    }
public void connectToServer() throws MqttException {
    String publisherId = UUID.randomUUID().toString();
    IMqttClient publisher = new MqttClient("localhost:1883",publisherId);

    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    publisher.connect(options);
}
public void connectWithHive(){

}*/

   public static void messageArrived(String topic, MqttMessage message) throws MqttException {
       // Called when a message arrives from the server that matches any
       // subscription made by the client
       String time = new Timestamp(System.currentTimeMillis()).toString();
       System.out.println("Time:\t" +time +
               "  Topic:\t" + topic +
               "  Message:\t" + new String(message.getPayload()) +
              "  QoS:\t" + message.getQos());
if(topic.equals("MQTTExamples")){
    DatabaseConnection databaseConnection = new DatabaseConnection();

    }



}
}
