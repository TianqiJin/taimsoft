package com.taimsoft.dbconnection;

import com.taimsoft.dbconnection.configuration.AppConfig;
import com.taimsoft.dbconnection.dao.IStaffService;
import com.taimsoft.dbconnection.dao.StaffServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by Tjin on 7/15/2017.
 */
public class DbConnectionMain {
    public static void main(String[] args){
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        IStaffService userService = (IStaffService) context.getBean("staffService");
        userService.getAllStaffs().forEach(u -> System.out.println(u.getStaffId()));

        context.close();
    }
}
