package com.taim.taimsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by dragonliu on 2017/8/21.
 */
@SpringBootApplication
@ComponentScan({"com.taim.taimsoft.dao","com.taim.taimsoft.service","com.taim.taimsoft.model","com.taim.taimsoft.controller","com.taim.taimsoft.configuration"})
public class Application {

    public static void main (String[] args){
        SpringApplication.run(Application.class,args);
    }
}
