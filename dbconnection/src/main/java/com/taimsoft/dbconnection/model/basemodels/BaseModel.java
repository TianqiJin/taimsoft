package com.taimsoft.dbconnection.model.basemodels;


import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import javax.persistence.*;

/**
 * Created by Tjin on 7/20/2017.
 */
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "date_created" ,nullable = false)
    private DateTime dateCreated;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "date_modified" ,nullable = false)
    private DateTime dateModified;

    public BaseModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public DateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(DateTime dateModified) {
        this.dateModified = dateModified;
    }
}
