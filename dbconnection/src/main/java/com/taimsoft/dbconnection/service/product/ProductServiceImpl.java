package com.taimsoft.dbconnection.service.product;

import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Product;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tjin on 8/19/2017.
 */
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IDao<Product> dao;

}
