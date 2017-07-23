package com.taimsoft.dbconnection.dao;

import com.taimsoft.dbconnection.model.Staff;

import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
public interface IStaffDao {
    void saveEmployee(Staff staff);

    List<Staff> getAllStaffs();

    Staff findByID(Integer staffID);

    void updateEmployee(Staff staff);

}
