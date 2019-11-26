package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;

public class DatabaseConnection {

    private Connection connection = null;
    private String statement = null;
    private PreparedStatement pStatement;
    private String query = null;

    public void updateDB(String topic, String message){
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection("", "", "");
            statement = "UPDATE mqttmessages SET message = '"+ message +"' WHERE topic = '" + topic + "'";
            pStatement = connection.prepareStatement(statement);
            pStatement.executeUpdate();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
