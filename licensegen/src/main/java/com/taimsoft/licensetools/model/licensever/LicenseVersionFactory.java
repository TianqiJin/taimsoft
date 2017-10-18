package com.taimsoft.licensetools.model.licensever;

import com.taimsoft.licensetools.model.LicenseFile;

/**
 * Created by tjin on 2017-02-09.
 */
public interface LicenseVersionFactory<T> {
    //create LicenseObject based on the input LicenseXML.LicenseAttrCollection object
    T create(Object o) throws LicenseFile.LicenseException, IllegalAccessException;
}
