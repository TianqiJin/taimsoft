package com.taim.backend.service.product;


import com.taim.backend.dao.product.ProductDaoImpl;
import com.taim.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */

@Service("productService")
@Transactional
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductDaoImpl dao;

    @Override
    public List<Product> getAllProducts() {
        return dao.getAll();
    }

    @Override
    public Product saveProduct(Product product)  {
       return dao.save(product);
    }

    @Override
    public Product getProductByTexture(String texture) {
        return dao.findByTexture(texture);
    }

    @Override
    public Product getProductById(Integer id) {
        return dao.findByID(id);
    }

    @Override
    public void deleteProduct(Product product) {
        dao.deleteObject(product);
    }

    @Override
    public Product updateProduct(Product product) {
       return dao.updateObject(product);
    }
}
