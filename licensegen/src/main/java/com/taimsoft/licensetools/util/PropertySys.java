package com.taimsoft.licensetools.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tjin on 2016-12-21.
 */
public class PropertySys {
    private static Logger logger = LogManager.getLogger(PropertySys.class);

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
