package com.taim.backend.service.property;

import com.taim.backend.dao.property.PropertyDaoImpl;
import com.taim.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("propertyService")
@Transactional
public class PropertyServiceImpl implements IPropertyService {
    @Autowired
    private PropertyDaoImpl dao;

    @Override
    public List<Property> getAllProperties() {
        return dao.getAll();
    }

    @Override
    public Property saveProperty(Property property) {
        return dao.save(property);
    }

    @Override
    public Property getPropertyById(Integer id) {
        return dao.findByID(id);
    }

    @Override
    public void deleteProperty(Property property) {
        dao.deleteObject(property);
    }

    @Override
    public Property updateProperty(Property property) {
        return dao.updateObject(property);
    }
}
