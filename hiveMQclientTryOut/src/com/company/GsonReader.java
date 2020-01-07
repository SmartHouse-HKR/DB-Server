package com.company;



import java.io.BufferedReader;
import java.io.FileReader;

import com.google.gson.Gson;

public class GsonReader {

    public DatabaseDetail databaseDetail;

    public DatabaseDetail jsonFile(){
        Gson gson = new Gson();
        try{
            BufferedReader buffReader = new BufferedReader(new FileReader("C:\\Users\\alice\\Desktop\\reallyTheDBFinal\\databasedetails\\databasedetails.json"));
            databaseDetail = gson.fromJson(buffReader, DatabaseDetail.class);
            //System.out.println("läser json-filen "+ deviceJavaObjectClass.toString());
        }catch(Exception e){
            e.printStackTrace();
        }

        return databaseDetail;
    }
}