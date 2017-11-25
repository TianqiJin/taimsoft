package com.taim.licensegen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tjin on 2016-12-21.
 */
public class PropertySys {
    private static Logger logger = LoggerFactory.getLogger(PropertySys.class);

    public static Properties getProperties(File file){
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(file)){
            logger.debug("Read property file from " + file.getAbsolutePath());
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            logger.error(e.getMessage() + "\nThe full stack trace is: ", e);
        }
        return null;
    }
}
