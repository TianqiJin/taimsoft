package com.taim.backend.service.license;

import com.taim.model.License;

import java.util.List;

public interface ILicenseService {
    List<License> getAllLicenses();
    License saveLicense(License license);
    License getLicenseById(Integer id);
    void deleteLicense(License license);
    License saveOrUpdateLicense(License license);
}
