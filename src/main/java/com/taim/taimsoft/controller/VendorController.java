package com.taim.taimsoft.controller;

import com.taim.taimsoft.model.Vendor;
import com.taim.taimsoft.service.vendor.IVendorService;
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
@RequestMapping(value = "/vendor")
public class VendorController {
    @Autowired
    private IVendorService service;

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> list = service.getAllVendors();
        return new ResponseEntity<List<Vendor>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByName")
    @ResponseBody
    public ResponseEntity<Vendor> getVendorByName(String name) {
        Vendor vendor = service.getVendorByName(name);
        return new ResponseEntity<Vendor>(vendor, HttpStatus.OK);
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<String> create(Vendor vendor) {
        try {
            service.saveVendor(vendor);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Vendor successfully saved!", HttpStatus.OK);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<String> update(Vendor vendor) {
        try {
            service.updateVendor(vendor);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Vendor successfully updated!", HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteVendorByName(String name) {
        Vendor vendor = service.getVendorByName(name);
        if (vendor !=null){
            service.deleteVendor(vendor);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such vendor found!", HttpStatus.OK);
        }
    }



}
