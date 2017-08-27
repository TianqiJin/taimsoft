package com.taim.taimsoft.service.vendor;

import com.taim.taimsoft.dao.IDao;
import com.taim.taimsoft.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */

@Service("vendorService")
@Transactional
public class VendorServiceImpl implements IVendorService{
    @Autowired
    private IDao<Vendor> dao;

    @Override
    public List<Vendor> getAllVendors() {
        return dao.getAll();
    }

    @Override
    public Vendor saveVendor(Vendor vendor) {
        return dao.save(vendor);
    }

    @Override
    public Vendor getVendorByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public void deleteVendor(Vendor vendor) {
        dao.deleteObject(vendor);
    }

    @Override
    public Vendor updateVendor(Vendor vendor) {
       return dao.updateObject(vendor);
    }
}
