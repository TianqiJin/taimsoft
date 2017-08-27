package com.taim.taimsoft.controller;

import com.taim.taimsoft.model.Product;
import com.taim.taimsoft.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/23.
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private IProductService service;

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list = service.getAllProducts();
        return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByTexture")
    @ResponseBody
    public ResponseEntity<Product> getProductByTexture(String texture) {
        Product product = service.getProductByTexture(texture);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById")
    @ResponseBody
    public ResponseEntity<Product> getProductById(Integer id) {
        Product product = service.getProductById(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }


    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<Product> create(Product product) {
        Product product1 = null;
        try {
            product1= service.saveProduct(product);
        } catch (Exception ex) {
            return new ResponseEntity<Product>(product1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Product>(product1, HttpStatus.OK);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<Product> update(Product product) {
        Product product1 = null;
        try {
            product1=service.updateProduct(product);
        } catch (Exception ex) {
            return new ResponseEntity<Product>(product1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Product>(product1, HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteProductById(Integer id) {
        Product product = service.getProductById(id);
        if (product !=null){
            service.deleteProduct(product);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such product found!", HttpStatus.OK);
        }
    }


}
