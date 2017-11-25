package com.taim.backend.service.license;

import com.taim.backend.dao.license.LicenseDaoImpl;
import com.taim.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("licenseService")
@Transactional
public class LicenseServiceImpl implements ILicenseService{
    @Autowired
    private LicenseDaoImpl dao;

    @Override
    public List<License> getAllLicenses() {
        return dao.getAll();
    }

    @Override
    public License saveLicense(License license) {
        return dao.save(license);
    }

    @Override
    public License getLicenseById(Integer id) {
        return dao.findByID(id);
    }

    @Override
    public void deleteLicense(License license) {
        dao.deleteObject(license);
    }

    @Override
    public License saveOrUpdateLicense(License license) {
        dao.saveOrUpdateObject(license);
        dao.flush();
        dao.refresh(license);

        return license;
    }
}
