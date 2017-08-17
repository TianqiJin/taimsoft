package com.taimsoft.dbconnection.service.staff;


import com.taimsoft.model.Staff;

import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
public interface IStaffService {
    List<Staff> getAllStaffs();
    void saveStaff(Staff staff);
    void updateStaff(Staff staff);
}
