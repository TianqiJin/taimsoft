package com.taim.backend.controller;

import com.taim.model.Product;
import com.taim.backend.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/23.
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private IProductService service;

    @RequestMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list = service.getAllProducts();
        return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByTexture",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Product> getProductByTexture(@RequestParam String texture) {
        Product product = service.getProductByTexture(texture);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Product> getProductById(@RequestParam Integer id) {
        Product product = service.getProductById(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }


    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product product1 = null;
        try {
            product1= service.saveProduct(product);
        } catch (Exception ex) {
            return new ResponseEntity<Product>(product1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Product>(product1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Product> update(@RequestBody Product product) {
        Product product1 = null;
        try {
            product1=service.updateProduct(product);
        } catch (Exception ex) {
            return new ResponseEntity<Product>(product1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Product>(product1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteProductById(@RequestParam Integer id) {
        Product product = service.getProductById(id);
        if (product !=null){
            service.deleteProduct(product);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such product found!", HttpStatus.OK);
        }
    }


}
