package com.taimsoft.licensetools.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.taimsoft.licensetools.util.AsciiConvert;
import com.taimsoft.licensetools.util.XmlProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tjin on 2016-11-25.
 */
public class LicenseFile {
    private FileAttributes fileAttributes;
    private LicenseAttributes licenseAttributes;
    private List<LicenseRequest> licenses;

    public LicenseFile(FileAttributes fileAttributes, LicenseAttributes licenseAttributes, List<LicenseRequest> licenses) {
        this.fileAttributes = fileAttributes;
        this.licenseAttributes = licenseAttributes;
        this.licenses = licenses;
    }

    public LicenseFile(JSONObject o) throws JSONException, LicenseException {
        fileAttributes = new FileAttributes(o.getJSONObject("file-attributes"));
        licenseAttributes = new LicenseAttributes(o.getJSONObject("license-attributes"));
        JSONArray licensesArray = o.getJSONArray("licenses");

        if(licensesArray.length() < 1) {
            throw new LicenseException("Licenses array cannot be empty");
        }
        licenses = new ArrayList<>();
        for(int i = 0; i < licensesArray.length(); ++i) {
            licenses.add(new LicenseRequest(licensesArray.getJSONObject(i)));
        }
    }

    public static class FileAttributes {
        @XmlProperty(name = "file-comment")
        private String comment;
        @XmlProperty(name = "record-id")
        private String recordId;

        public FileAttributes( String comment, String recordId) {
            this.comment = comment;
            this.recordId = recordId;
        }

        public FileAttributes(JSONObject o) throws JSONException, LicenseException {
            comment = o.optString("comment");
            recordId = o.getString("record-id");
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }
    }

    public static class LicenseAttributes {
        @XmlProperty(name = "customer")
        private String customer = null;
        @XmlProperty(name = "valid-until")
        private Long validUntil = null;
        @XmlProperty(name = "project")
        private String project;

        public LicenseAttributes(String customer, String project, Long validUntil) {
            this.customer = AsciiConvert.MungeTextIntoAscii(customer);
            this.project = project;
            this.validUntil = validUntil;
        }



        public LicenseAttributes(JSONObject o) throws JSONException, LicenseException {
            customer = AsciiConvert.MungeTextIntoAscii(o.getString("customer"));
            validUntil = o.getLong("valid-until");
            project = o.getString("project");
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = AsciiConvert.MungeTextIntoAscii(customer);
        }

        public Long getValidUntil() {
            return validUntil;
        }

        public void setValidUntil(Long validUntil) {
            this.validUntil = validUntil;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

    }

    public static class LicenseRequest {
        private JSONObject json;

        public LicenseRequest(JSONObject o) throws JSONException {
            json = o;
        }
        public LicenseRequest(Map<String, Object> attributes) throws JSONException {
            json = new JSONObject();
            for(Map.Entry<String, Object> entry : attributes.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
        }
        public JSONObject getJson() {
            return json;
        }

        public void setJson(JSONObject json) {
            this.json = json;
        }
    }


    public static class LicenseException extends Exception {
        public LicenseException(String s) {
            super(s);
        }
        public LicenseException(String s, Exception x) {
            super(s, x);
        }
    }

    public FileAttributes getFileAttributes() {
        return fileAttributes;
    }

    public void setFileAttributes(FileAttributes fileAttributes) {
        this.fileAttributes = fileAttributes;
    }

    public LicenseAttributes getLicenseAttributes() {
        return licenseAttributes;
    }

    public void setLicenseAttributes(LicenseAttributes licenseAttributes) {
        this.licenseAttributes = licenseAttributes;
    }

    public List<LicenseRequest> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<LicenseRequest> licenses) {
        this.licenses = licenses;
    }
}
