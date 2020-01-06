package com.company;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

public class MqttConnections {
    String topic;

    int qos;
    String broker;
    String clientID;
    MemoryPersistence persistence;
    char[] password = new char[2];
    MqttClient mqttClient;
    MqttMessage message;
DatabaseConnection db = new DatabaseConnection();
ArrayList voltmeasurements;
    public void MakeAconnect() throws MqttException {
        password[0] = 'a';
        password[1] = 'b';
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setPassword(password);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName("admin-user");
        mqttConnectOptions.setMaxInflight(60);
        mqttConnectOptions.setAutomaticReconnect(true);

        broker = "tcp://localhost:1883";
        qos = 2;
        clientID = "JavaSample";
        persistence = new MemoryPersistence();
        mqttClient = new MqttClient(broker, clientID, persistence);
        mqttClient.setCallback(new SubscribeCallback());


        System.out.println("running");

        mqttClient.connect(mqttConnectOptions);
        mqttClient.subscribe("smarthouse/#");
        mqttClient.subscribe("smarthouse/voltage/value");

        System.out.println("running ");
      if(mqttClient.isConnected()==true){
        ArrayList<mqttMessageObject> mqttMessageObjectArrayList = db.onStart();
        for (mqttMessageObject mqttMsg : mqttMessageObjectArrayList) {
            publishMqttMessage(mqttMsg.getTopic(),mqttMsg.getMessage());
        }}
      else{
          System.out.println("the broker is down");
      }

    }


    public void publishMqttMessage(String topic, String messageRaw) throws MqttException {
if(mqttClient.isConnected()==true) {
    message = new MqttMessage(messageRaw.getBytes());
    mqttClient.publish(topic, message);
}
else System.out.println("broker is down");

    }
public void printOutLast20VoltageRecordings() throws MqttException {
 mqttClient.subscribe("smarthouse/voltage/overview");

    ArrayList<mqttMessageObject> mqttMessageObjectArrayList = db.onStart();
    voltmeasurements = new ArrayList();
    for (mqttMessageObject voltMessage: mqttMessageObjectArrayList
    ) {
        if(voltMessage.getTopic().equals("smarthouse/voltage/overview")){
                voltmeasurements.add(voltMessage.getMessage());

        }
    }
}

}
