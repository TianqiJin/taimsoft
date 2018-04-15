package com.taim.backend.controller;

import com.taim.backend.service.license.ILicenseService;
import com.taim.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/license")
public class LicenseController {
    @Autowired
    private ILicenseService service;

    @RequestMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<License>> getAllLicenses() {
        List<License> list = service.getAllLicenses();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<License> getLicenseById(@RequestParam Long id) {
        License License = service.getLicenseById(id);
        return new ResponseEntity<>(License, HttpStatus.OK);
    }

    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<License> create(@RequestBody License License) {
        License License1 = service.saveLicense(License);
        return new ResponseEntity<>(License1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/saveOrUpdate",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<License> saveOrUpdate(@RequestBody License License) {
        License License1 = service.saveOrUpdateLicense(License);
        return new ResponseEntity<>(License1, HttpStatus.ACCEPTED);
    }

}
