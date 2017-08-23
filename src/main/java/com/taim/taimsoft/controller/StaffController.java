package com.taim.taimsoft.controller;

import com.taim.taimsoft.model.Staff;
import com.taim.taimsoft.service.staff.IStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/22.
 */
@Controller
@RequestMapping(value = "/staff")
public class StaffController {
    @Autowired
    private IStaffService service;

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public ResponseEntity<List<Staff>> getAllStaffs() {
        List<Staff> list = service.getAllStaffs();
        return new ResponseEntity<List<Staff>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByName")
    @ResponseBody
    public ResponseEntity<Staff> getStaffByName(String name) {
        Staff staff = service.getStaffByName(name);
        return new ResponseEntity<Staff>(staff, HttpStatus.OK);
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<String> create(Staff staff) {
        try {
            service.saveStaff(staff);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Staff successfully saved!", HttpStatus.OK);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<String> update(Staff staff) {
        try {
            service.updateStaff(staff);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Staff successfully updated!", HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteStaffByName(String name) {
        Staff staff = service.getStaffByName(name);
        if (staff !=null){
            service.deleteStaff(staff);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such staff found!", HttpStatus.OK);
        }
    }


}
