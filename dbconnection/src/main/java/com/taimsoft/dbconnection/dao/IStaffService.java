package com.taimsoft.dbconnection.dao;

import com.taimsoft.dbconnection.model.Staff;

import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
public interface IStaffService {
    List<Staff> getAllStaffs();
    void saveEmployee(Staff staff);
}
