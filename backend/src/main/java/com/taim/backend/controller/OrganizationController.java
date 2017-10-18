package com.taim.backend.controller;

import com.taim.model.Organization;
import com.taim.backend.service.organization.IOrganizationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/21.
 */
@Controller
@RequestMapping(value = "/organization")
public class OrganizationController {
    @Autowired
    private IOrganizationService service;

    @RequestMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> list = service.getAllOrganizations();
        return new ResponseEntity<List<Organization>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByName",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Organization> getOrganizationByName(@RequestParam String name) {
        Organization organization = service.getOrganizationByName(name);
        return new ResponseEntity<Organization>(organization, HttpStatus.OK);
    }

    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Organization> create(@RequestBody Organization organization) {
        Organization organization1 = service.saveOrganization(organization);
        return new ResponseEntity<Organization>(organization1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Organization> update(@RequestBody Organization organization) {
        Organization organization1 = service.updateOrganization(organization);
        return new ResponseEntity<Organization>(organization1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteOrganizationByName(@RequestParam String name) {
        Organization organization = service.getOrganizationByName(name);
        if (organization !=null){
            service.deleteOrganization(organization);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such organization found!", HttpStatus.OK);
        }
    }
}
