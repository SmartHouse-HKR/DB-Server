package com.company;

public class mqttMessageObject {

    private int id;
    private String topic;
    private String message;

    public mqttMessageObject(int id, String topic, String message){
        this.id = id;
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
