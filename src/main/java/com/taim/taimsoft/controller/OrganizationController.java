package com.taim.taimsoft.controller;

import com.taim.taimsoft.model.Organization;
import com.taim.taimsoft.service.organization.IOrganizationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/21.
 */
@Controller
@RequestMapping(value = "/organization")
public class OrganizationController {
    @Autowired
    private IOrganizationService service;

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> list = service.getAllOrganizations();
        return new ResponseEntity<List<Organization>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByName")
    @ResponseBody
    public ResponseEntity<Organization> getOrganizationByName(String name) {
        Organization organization = service.getOrganizationByName(name);
        return new ResponseEntity<Organization>(organization, HttpStatus.OK);
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<String> create(Organization organization) {
        try {
            service.saveOrganization(organization);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Organization successfully saved!", HttpStatus.OK);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<String> update(Organization organization) {
        try {
            service.updateOrganization(organization);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Organization successfully updated!", HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteOrganizationByName(String name) {
        Organization organization = service.getOrganizationByName(name);
        if (organization !=null){
            service.deleteOrganization(organization);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such organization found!", HttpStatus.OK);
        }
    }



}
