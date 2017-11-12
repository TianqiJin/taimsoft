package com.taim.backend.service.vendor;

import com.taim.model.Vendor;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public interface IVendorService {
    List<Vendor> getAllVendors();
    Vendor saveVendor(Vendor vendor);
    Vendor getVendorByName(String name);
    void deleteVendor(Vendor vendor);
    Vendor updateVendor(Vendor vendor);
    Vendor saveOrUpdateVendor(Vendor vendor);
}
