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
    private ArrayList<Double> voltageOverview;
    private CeilingLight cl = null;
    private StandingLight sl = null;
    private Fan fan = null;
    private final String voltageValue = "smarthouse/voltage/value";
    private final String btFanState = "smarthouse/bt_fan1/state";
    private final String btFanSwing = "smarthouse/bt_fan1/swing";
    private final String btFanSpeed = "smarthouse/bt_fan1/speed";
    private final String btLightState = "smarthouse/bt_light1/state";
    private final String btLampState = "smarthouse/bt_lamp1/state";
    private final String btFanMode = "smarthouse/bt_fan1/mode";

    public void MakeAconnect() throws MqttException {
        db = new DatabaseConnection();
        voltageOverview = new ArrayList<>();
        password[0] = 'a';
        password[1] = 'b';
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
       // mqttConnectOptions.setPassword(password);
        mqttConnectOptions.setCleanSession(true);
        //mqttConnectOptions.setUserName("admin-user");
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
        System.out.println("running ");


    }


    public void publishMqttMessage(String topic, String messageRaw) throws MqttException {
        if(mqttClient.isConnected()==true) {
            message = new MqttMessage(messageRaw.getBytes());
            message.setRetained(true);
            mqttClient.publish(topic, message);
        } else
            System.out.println("broker is down");
    }

    public void printOutLast20VoltageRecordings() throws MqttException {
    mqttClient.subscribe("smarthouse/voltage/overview");

        ArrayList<mqttMessageObject> mqttMessageObjectArrayList = db.onStart();
        voltmeasurements = new ArrayList();
        for (mqttMessageObject voltMessage: mqttMessageObjectArrayList) {
            if(voltMessage.getTopic().equals("smarthouse/voltage/overview")){
                    voltmeasurements.add(voltMessage.getMessage());

            }
        }
    }

    @Override
    public void connectComplete(boolean b, String s) {
        //publishing latest messages from database
        try {
            ArrayList<mqttMessageObject> mqttMessageObjectArrayList = db.onStart();
            for (mqttMessageObject mqttMsg : mqttMessageObjectArrayList) {
                publishMqttMessage(mqttMsg.getTopic(), mqttMsg.getMessage());
            }
            //subscribing to all smarthouse topics
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
        System.out.println(throwable.toString());
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String mqttMessageString = mqttMessage.toString();
        System.out.println(mqttMessageString);

        //prints out a message to show which topic it is and the mssage and saves to database.
        System.out.println("Topic: " + topic);
        System.out.println("Message: " + mqttMessageString);
        System.out.println();
        if(!topic.equals("smarthouse/voltage/value") || !topic.equals("smarthouse/voltage/overview")){
            System.out.println(mqttMessageString);
            db.updateMessages(topic, mqttMessageString);
        }

        switch (topic) {
            case voltageValue:
                System.out.println(10);
                double voltage = Double.parseDouble(mqttMessageString);
                db.updateVoltage(voltage);
                voltageOverview.add(voltage);
                String json = new Gson().toJson(voltageOverview);
                MqttMessage jsonMessage = new MqttMessage();
                jsonMessage.setPayload(json.getBytes());
                mqttClient.publish("smarthouse/voltage/overview", jsonMessage);
                break;
            case btFanState:
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
            case btFanSwing:
                fan.changeSwing();
                System.out.println("swinging fan");
                break;
            case btFanSpeed:
                if(mqttMessageString.equals("higher")){
                    fan.speedUp();
                    System.out.println("fan up");
                }else{
                    fan.speedDown();
                    System.out.println("fan down");
                }
                break;
            case btLightState:
                cl.turnOnSet(mqttMessageString);
                break;
            case btLampState:
                if(mqttMessageString.equals("on")){
                    sl.turnOnLamp();
                }else{
                    sl.turnOffLamp();
                }
                break;
            case btFanMode:
                fan.changeMode();
                break;
            default:
                break;
        }

        /*if(!topic.equals("smarthouse/voltage/value") || !topic.equals("smarthouse/voltage/overview")){
            if(topic.equals("smarthouse/bt_fan1/state") && mqttMessageString.equals("on")){
                fan.turnOnFan();
            }
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
