package com.taim.backend.service.product;

import com.taim.model.Product;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public interface IProductService {
    List<Product> getAllProducts();
    Product saveProduct(Product Product);
    Product getProductByTexture(String texture);
    Product getProductById(Integer id);
    void deleteProduct(Product product);
    Product updateProduct(Product product);
}
