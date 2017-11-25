package com.taim.backend.dao.staff;


import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Staff;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
@Repository("staffDao")
public class StaffDaoImpl extends AbstractDao implements IDao<Staff> {

    @Override
    @SuppressWarnings("unchecked")
    public List<Staff> getAll() {
        Criteria criteria = getSession().createCriteria(Staff.class);
        criteria.add(Restrictions.eq("deleted",false));
        return (List<Staff>) criteria.list();
    }

    @Override
    public Staff save(Staff staff) {
        persist(staff);
        return staff;
    }

    @Override
    public Staff findByID(Integer staffID) {
        Criteria criteria = getSession().createCriteria(Staff.class);
        criteria.add(Restrictions.eq("id", staffID));
        criteria.add(Restrictions.eq("deleted",false));
        return (Staff) criteria.uniqueResult();
    }

    @Override
    public Staff updateObject(Staff staff) {
        update(staff);
        return staff;
    }

    @Override
    public Staff saveOrUpdateObject(Staff object) {
        saveOrUpdate(object);
        return object;
    }

    @Override
    public Staff findByName(String name){
    Criteria criteria = getSession().createCriteria(Staff.class);
    criteria.add(Restrictions.eq("userName", name));
    criteria.add(Restrictions.eq("deleted",false));
    return (Staff)criteria.uniqueResult();
    }

    @Override
    public void deleteObject(Staff staff) {
        delete(staff);
    }
}
