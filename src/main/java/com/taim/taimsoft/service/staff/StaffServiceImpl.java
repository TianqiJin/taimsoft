package com.taim.taimsoft.service.staff;


import com.taim.taimsoft.dao.IDao;
import com.taim.taimsoft.model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Tjin on 7/15/2017.
 */
@Service("staffService")
@Transactional
public class StaffServiceImpl implements IStaffService {

    @Autowired
    private IDao<Staff> dao;

    public List<Staff> getAllStaffs() {
        return dao.getAll();
    }

    @Override
    public void saveStaff(Staff staff) {
        dao.save(staff);
    }

    @Override
    public void updateStaff(Staff staff){
        dao.updateObject(staff);
    }

    @Override
    public Staff getStaffByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public void deleteStaff(Staff staff) {
        dao.deleteObject(staff);
    }

}
