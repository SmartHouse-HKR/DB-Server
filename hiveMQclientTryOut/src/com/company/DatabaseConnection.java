package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;

public class DatabaseConnection {


    private GsonReader gsonReader;
    private DatabaseDetail details;
    private Connection connection = null;
    private String statement = null;
    private PreparedStatement pStatement;
    private String query = null;
    
    public void updateDB(){
        try{
            gsonReader = new GsonReader();
            details = gsonReader.jsonFile();
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            connection = DriverManager.getConnection(details.getUrl(), details.getUser(), details.getPassword());
            statement = "UPDATE devices SET deviceName = 'notLight' WHERE idDevices = 1";
            pStatement = connection.prepareStatement(statement);
            pStatement.executeUpdate();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
