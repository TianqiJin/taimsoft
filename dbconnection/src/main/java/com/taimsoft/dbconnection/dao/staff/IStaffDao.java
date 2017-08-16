package com.taimsoft.dbconnection.dao.staff;

import com.taimsoft.dbconnection.model.Staff;

import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
public interface IStaffDao {
    void saveStaff(Staff staff);

    List<Staff> getAllStaffs();

    Staff findByID(Integer staffID);

    void updateStaff(Staff staff);

}