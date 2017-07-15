package com.taimsoft.dbconnection.dao;

import com.taimsoft.dbconnection.model.Staff;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
@Repository("staffDao")
public class StaffDaoImpl extends AbstractDao implements IStaffDao {

    @SuppressWarnings("unchecked")
    public List<Staff> getAllStaffs() {
        Criteria criteria = getSession().createCriteria(Staff.class);
        return (List<Staff>) criteria.list();
    }
}
