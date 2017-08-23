package com.taim.taimsoft.service.vendor;

import com.taim.taimsoft.model.Vendor;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public interface IVendorService {
    List<Vendor> getAllVendors();
    void saveVendor(Vendor vendor);
    Vendor getVendorByName(String name);
    void deleteVendor(Vendor vendor);
    void updateVendor(Vendor vendor);
}
