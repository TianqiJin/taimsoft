package com.taim.backend.controller;

import com.taim.model.Vendor;
import com.taim.backend.service.vendor.IVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/22.
 */

@Controller
@RequestMapping(value = "/vendor")
public class VendorController {
    @Autowired
    private IVendorService service;

    @RequestMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> list = service.getAllVendors();
        return new ResponseEntity<List<Vendor>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByName",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Vendor> getVendorByName(@RequestParam String name) {
        Vendor vendor = service.getVendorByName(name);
        return new ResponseEntity<Vendor>(vendor, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Vendor> getVendorByName(@RequestParam Integer id) {
        Vendor vendor = service.getVendorById(id);
        return new ResponseEntity<Vendor>(vendor, HttpStatus.OK);
    }

    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Vendor> create(@RequestBody Vendor vendor) {
        Vendor vendor1 = service.saveVendor(vendor);
        return new ResponseEntity<Vendor>(vendor1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Vendor> update(@RequestBody Vendor vendor) {
        Vendor vendor1 = service.updateVendor(vendor);
        return new ResponseEntity<Vendor>(vendor1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/saveOrUpdate",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Vendor> saveOrUpdate(@RequestBody Vendor vendor) {
        Vendor vendor1 = service.saveOrUpdateVendor(vendor);
        return new ResponseEntity<Vendor>(vendor1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteVendorByName(@RequestParam String name) {
        Vendor vendor = service.getVendorByName(name);
        if (vendor !=null){
            service.deleteVendor(vendor);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such vendor found!", HttpStatus.OK);
        }
    }
}
