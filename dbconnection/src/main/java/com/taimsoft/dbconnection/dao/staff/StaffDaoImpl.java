package com.taimsoft.dbconnection.dao.staff;

import com.taimsoft.dbconnection.dao.AbstractDao;
import com.taimsoft.dbconnection.dao.staff.IStaffDao;
import com.taimsoft.dbconnection.model.Staff;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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

    @Override
    public void saveStaff(Staff staff) {
        persist(staff);
    }

    @Override
    public Staff findByID(Integer staffID) {
        Criteria criteria = getSession().createCriteria(Staff.class);
        criteria.add(Restrictions.eq("id", staffID));
        return (Staff) criteria.uniqueResult();
    }

    @Override
    public void updateStaff(Staff staff) {
        update(staff);
    }
}
