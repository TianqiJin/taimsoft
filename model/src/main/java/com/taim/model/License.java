package com.taim.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "license")
public class License extends BaseModel {
    @Column(name = "license_file", nullable = false)
    private byte[] licenseFile;

    public License(){}

    public byte[] getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(byte[] licenseFile) {
        this.licenseFile = licenseFile;
    }
}
