package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LicenseDTO extends BaseModelDTO {
    private ObjectProperty<byte[]> licenseFile;

    public LicenseDTO(){
        licenseFile = new SimpleObjectProperty<>();
    }

    public byte[] getLicenseFile() {
        return licenseFile.get();
    }

    public ObjectProperty<byte[]> licenseFileProperty() {
        return licenseFile;
    }

    public void setLicenseFile(byte[] licenseFile) {
        this.licenseFile.set(licenseFile);
    }
}
