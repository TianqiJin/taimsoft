package com.taim.licensegen.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tjin on 2016-12-21.
 */
public class PropertySys {

    public static Properties getProperties(File file){
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(file)){
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
