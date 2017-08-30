package com.taim.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by dragonliu on 2017/8/21.
 */
@SpringBootApplication
@ComponentScan({"com.taim.backend.dao","com.taim.backend.service","com.taim.model","com.taim.backend.controller","com.taim.backend.configuration"})
public class Application {

    public static void main (String[] args){
        SpringApplication.run(Application.class,args);
    }
}
