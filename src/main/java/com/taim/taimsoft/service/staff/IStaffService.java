package com.taim.taimsoft.service.staff;



import com.taim.taimsoft.model.Staff;

import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
public interface IStaffService {
    List<Staff> getAllStaffs();
    void saveStaff(Staff staff);
    void updateStaff(Staff staff);
    Staff getStaffByName(String name);
    void deleteStaff(Staff staff);
}
