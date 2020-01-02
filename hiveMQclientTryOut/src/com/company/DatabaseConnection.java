package com.company;

import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {


    private GsonReader gsonReader;
    private DatabaseDetail details;
    private Connection connection = null;
    private String statement = null;
    private PreparedStatement pStatement;
    private String query = null;


    public void updateMessages(String topic, String message){

        try{
            gsonReader = new GsonReader();
            details = gsonReader.jsonFile();
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(details.getUrl(), details.getUser(), details.getPassword());
            statement = "UPDATE latestmessages SET message = '"+ message +"' WHERE topic = '" + topic + "'";
            pStatement = connection.prepareStatement(statement);
            pStatement.executeUpdate();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateVoltage(int voltage){
        try {
            gsonReader = new GsonReader();
            details = gsonReader.jsonFile();
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(details.getUrl(), details.getUser(), details.getPassword());
            statement = "UPDATE smarthouse SET voltage = voltage+" + voltage + " WHERE id = 1";
            pStatement = connection.prepareStatement(statement);
            pStatement.executeUpdate();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setState(String value){
        try {
            gsonReader = new GsonReader();
            details = gsonReader.jsonFile();
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            //connection = DriverManager.getConnection(details.getUrl(), details.getUser(), details.getPassword());
            connection = DriverManager.getConnection(details.getUrl(), details.getUser(), details.getPassword());
            statement = "UPDATE lights SET light_state = '" + value +"' WHERE id = 1";
            pStatement = connection.prepareStatement(statement);
            pStatement.executeUpdate();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<mqttMessageObject> onStart(){
        ArrayList<mqttMessageObject> messages = new ArrayList<>();
        try{
            gsonReader = new GsonReader();
            details = gsonReader.jsonFile();
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            //connection = DriverManager.getConnection(details.getUrl(), details.getUser(), details.getPassword());
            connection = DriverManager.getConnection(details.getUrl(), details.getUser(), details.getPassword());
            statement = "SELECT * FROM latestmessages";
            pStatement = connection.prepareStatement(statement);
            ResultSet rs = pStatement.executeQuery(statement);

            while(rs.next()){
                messages.add(new mqttMessageObject(rs.getInt("idlatestmessages"), rs.getString("topi"), rs.getString("message")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }




}
