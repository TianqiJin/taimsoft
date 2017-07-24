package com.taimsoft.dbconnection;

import com.taimsoft.dbconnection.configuration.AppConfig;
import com.taimsoft.dbconnection.service.staff.IStaffService;
import com.taimsoft.dbconnection.model.Staff;
import org.joda.time.DateTime;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by Tjin on 7/15/2017.
 */
public class DbConnectionMain {
    public static void main(String[] args){
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        IStaffService userService = (IStaffService) context.getBean("staffService");

        Staff staff = new Staff();
        staff.setUserName("tjin");
        staff.setFullName("Tianqi Jin");
        staff.setPassword("testpassword");
        staff.setDateCreated(DateTime.now());
        staff.setDateModified(DateTime.now());

        userService.saveEmployee(staff);
        context.close();
    }
}
