package com.taim.backend.service.staff;



import com.taim.model.Staff;

import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
public interface IStaffService {
    List<Staff> getAllStaffs();
    Staff saveStaff(Staff staff);
    Staff updateStaff(Staff staff);
    Staff getStaffByName(String name);
    Staff getStaffById(Integer id);
    Staff saveOrUpdateStaff(Staff staff);
    void deleteStaff(Staff staff);
}
