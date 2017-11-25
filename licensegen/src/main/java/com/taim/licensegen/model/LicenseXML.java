package com.taim.licensegen.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tjin on 2016-12-06.
 */
public class LicenseXML {
    private List<LicenseAttrCollection> licenses;

    public LicenseXML(){
        licenses = new ArrayList<>();
    }

    public List<LicenseAttrCollection> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<LicenseAttrCollection> licenses) {
        this.licenses = licenses;
    }

    public static class LicenseAttrCollection{
        private Map<String, Object> attributes;

        public LicenseAttrCollection(){
            attributes = new HashMap<>();
        }

        public LicenseAttrCollection(JSONObject json){
            attributes = new HashMap<>();
            for(String s: json.keySet()){
                attributes.put(s, json.get(s));
            }
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public boolean isKeyPresented(String key){
            return this.attributes.containsKey(key);
        }

        public boolean isKeyValueNull(String key){
            return this.attributes.get(key) == null;
        }
    }



}
