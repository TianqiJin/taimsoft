package com.taimsoft.dbconnection.service.vendor;

import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tjin on 8/19/2017.
 */
public class VendorServiceImpl implements IVendorService{
    @Autowired
    private IDao<Vendor> dao;

}
