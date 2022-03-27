package com.cydeo.utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationReader {
    private static Properties properties;

    static {

        try {
            String path="configuration.properties";
            FileInputStream fileInput= new FileInputStream(path);
            properties=new Properties();
            properties.load(fileInput);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String get(String key){
        return properties.getProperty(key);
    }


}
