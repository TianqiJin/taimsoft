package com.taim.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dragonliu on 2017/8/27.
 */
public class PropertiesProcessor {

    public static Properties properties;

    public static String serverUrl;

    static {
        properties = new Properties();
        InputStream input = null;

        try {

            try {
                String filePath = new File("").getAbsolutePath();
                input = new FileInputStream(filePath + "/src/main/resources/config");
                System.out.println("Local config.BigDataProcessor file found!. " + filePath + "/src/main/resources/config");
            } catch (Exception e) {
                input = PropertiesProcessor.class.getClassLoader().getResourceAsStream("config");
                System.out.println("could not find local config file. Using the one in the Jar");
            }

            properties.load(input);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        serverUrl=properties.getProperty("serverPath");

    }

}
