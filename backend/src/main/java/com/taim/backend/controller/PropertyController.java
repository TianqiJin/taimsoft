package com.taim.backend.controller;

import com.taim.backend.service.property.IPropertyService;
import com.taim.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/property")
public class PropertyController {

    @Autowired
    private IPropertyService service;

    @RequestMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> list = service.getAllProperties();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @RequestMapping(value = "/getById",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Property> getPropertyById(@RequestParam Long id) {
        Property Property = service.getPropertyById(id);
        return new ResponseEntity<>(Property, HttpStatus.OK);
    }

    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Property> create(@RequestBody Property Property) {
        Property Property1 = service.saveProperty(Property);
        return new ResponseEntity<>(Property1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Property> update(@RequestBody Property Property) {
        Property Property1 = service.updateProperty(Property);
        return new ResponseEntity<>(Property1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/saveOrUpdate",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Property> saveOrUpdate(@RequestBody Property property) {
        Property property1 = service.saveOrUpdateProperty(property);
        return new ResponseEntity<>(property1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deletePropertyById(@RequestParam Long id) {
        Property Property = service.getPropertyById(id);
        if (Property !=null){
            service.deleteProperty(Property);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such Property found!", HttpStatus.OK);
        }
    }
}
