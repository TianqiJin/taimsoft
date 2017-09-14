package com.taim.backend.controller;

import com.taim.model.Staff;
import com.taim.backend.service.staff.IStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Staff> getStaffByName(@RequestParam String name) {
        Staff staff = service.getStaffByName(name);
        return new ResponseEntity<Staff>(staff, HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Staff> create(@RequestBody Staff staff) {
        Staff staff1 = null;
        try {
            staff1=service.saveStaff(staff);
        } catch (Exception ex) {
            return new ResponseEntity<Staff>(staff1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Staff>(staff1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Staff> update(@RequestBody Staff staff) {
        Staff staff1 = null;
        try {
            staff1=service.updateStaff(staff);
        } catch (Exception ex) {
            return new ResponseEntity<Staff>(staff1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Staff>(staff1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteStaffByName(@RequestParam String name) {
        Staff staff = service.getStaffByName(name);
        if (staff !=null){
            service.deleteStaff(staff);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such staff found!", HttpStatus.OK);
        }
    }


}