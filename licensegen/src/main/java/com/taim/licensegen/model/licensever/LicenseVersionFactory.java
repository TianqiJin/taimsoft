package com.taim.licensegen.model.licensever;

import com.taim.licensegen.model.LicenseFile;

/**
 * Created by tjin on 2017-02-09.
 */
public interface LicenseVersionFactory<T> {
    //create LicenseObject based on the input LicenseXML.LicenseAttrCollection object
    T create(Object o) throws LicenseFile.LicenseException, IllegalAccessException;
}
