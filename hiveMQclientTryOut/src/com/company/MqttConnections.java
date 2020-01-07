package com.company;

import com.bluetooth.*;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;

public class MqttConnections implements MqttCallbackExtended {
    String topic;

    int qos;
    String broker;
    String clientID;
    MemoryPersistence persistence;
    char[] password = new char[2];
    MqttClient mqttClient;
    MqttMessage message;
    private DatabaseConnection db;
    ArrayList voltmeasurements;
    private ArrayList<Integer> voltageOverview;
    private CeilingLight cl = null;
    private StandingLight sl = null;
    private Fan fan = null;

    public void MakeAconnect() throws MqttException {
        db = new DatabaseConnection();
        voltageOverview = new ArrayList<>();
        password[0] = 'a';
        password[1] = 'b';
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setPassword(password);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName("admin-user");
        mqttConnectOptions.setMaxInflight(60);
        mqttConnectOptions.setAutomaticReconnect(true);

       //broker = "tcp://192.168.0.186:1883";
       broker = "tcp://localhost:1883";
        qos = 2;
        clientID = "JavaSample";
        persistence = new MemoryPersistence();
        mqttClient = new MqttClient(broker, clientID, persistence);
        mqttClient.setCallback(this);


        System.out.println("running");

        mqttClient.connect(mqttConnectOptions);
        //mqttClient.subscribe("smarthouse/#");
        //mqttClient.subscribe("smarthouse/voltage/value");
        System.out.println("running ");
        /*
      if(mqttClient.isConnected()==true){
        ArrayList<mqttMessageObject> mqttMessageObjectArrayList = db.onStart();
        for (mqttMessageObject mqttMsg : mqttMessageObjectArrayList) {
            publishMqttMessage(mqttMsg.getTopic(),mqttMsg.getMessage());
        }}
      else{
          System.out.println("the broker is down");
      }*/

    }


    public void publishMqttMessage(String topic, String messageRaw) throws MqttException {
if(mqttClient.isConnected()==true) {
    message = new MqttMessage(messageRaw.getBytes());
    message.setRetained(true);
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

    @Override
    public void connectComplete(boolean b, String s) {
        try {
            ArrayList<mqttMessageObject> mqttMessageObjectArrayList = db.onStart();
            for (mqttMessageObject mqttMsg : mqttMessageObjectArrayList) {
                publishMqttMessage(mqttMsg.getTopic(), "");
            }

            mqttClient.subscribe("smarthouse/#");

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
        } catch (MqttException e ) {
            e.printStackTrace();
        }


    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println(" I LOST HOUSTON");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String mqttMessageString = mqttMessage.toString();
        System.out.println(mqttMessageString);

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
                if(mqttMessageString.equals("on"))
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
                if(mqttMessageString.equals("higher")){
                    fan.speedUp();
                    System.out.println("fan up");
                }else{
                    fan.speedDown();
                    System.out.println("fan down");
                }
                break;
            case "smarthouse/bt_light/state":
                if(mqttMessageString.equals("1111")){
                    cl.turnOnSet("1111");
                }else{
                    cl.turnOnSet("0001");
                }
                break;
            case "smarthouse/bt_lamp/state":
                if(mqttMessageString.equals("on")){
                    sl.turnOnLamp();
                }else{
                    sl.turnOffLamp();
                }
                break;
            default:
                break;
        }

        /*if(!topic.equals("smarthouse/voltage/value") || !topic.equals("smarthouse/voltage/overview")){
            db.updateMessages(topic, mqttMessageString);
            System.out.println("Is now On " + topic);
        }

        if(topic.equals("smarthouse/voltage/value")){
            int voltage = Integer.parseInt(mqttMessageString);
            db.updateVoltage(voltage);
            voltageOverview.add(voltage);
            String json = new Gson().toJson(voltageOverview);
            MqttMessage jsonMessage = new MqttMessage();
            jsonMessage.setPayload(json.getBytes());
            mqttClient.publish("smarthouse/voltage/overview", jsonMessage);

        }*/


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
