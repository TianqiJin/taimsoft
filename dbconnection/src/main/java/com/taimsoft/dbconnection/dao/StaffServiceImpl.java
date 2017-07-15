package com.taimsoft.dbconnection.dao;

import com.taimsoft.dbconnection.model.Staff;
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
    private IStaffDao dao;

    public List<Staff> getAllStaffs() {
        return dao.getAllStaffs();
    }

}
