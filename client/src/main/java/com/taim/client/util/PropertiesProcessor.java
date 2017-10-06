package com.taim.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dragonliu on 2017/8/27.
 */
public class PropertiesProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesProcessor.class);

    public static Properties properties;

    public static String serverUrl;

    static {
        properties = new Properties();
        InputStream input = null;
        try {
            try {
                String filePath = new File("").getAbsolutePath();
                input = new FileInputStream(filePath + "/src/main/resources/config.properties");
                logger.debug("Local config.properties.BigDataProcessor file found!. " + filePath + "/src/main/resources/config.properties");
            } catch (Exception e) {
                input = PropertiesProcessor.class.getClassLoader().getResourceAsStream("config.properties");
                logger.error("could not find local config.properties file. Using the one in the Jar");
            }
            properties.load(input);
        } catch (IOException ex) {
            logger.error("Error reading config.properties: ", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignore) {}
            }
        }
        serverUrl=properties.getProperty("serverPath");
    }

}
