package com.taim.backend.service.property;

import com.taim.model.Property;

import java.util.List;

public interface IPropertyService {
    List<Property> getAllProperties();
    Property saveProperty(Property property);
    Property getPropertyById(Integer id);
    void deleteProperty(Property property);
    Property updateProperty(Property property);
}
