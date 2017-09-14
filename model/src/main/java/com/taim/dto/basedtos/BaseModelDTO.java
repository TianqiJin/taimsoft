package com.taim.dto.basedtos;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.joda.time.DateTime;

public class BaseModelDTO {
    private IntegerProperty id;
    private ObjectProperty<DateTime> dateCreated;
    private ObjectProperty<DateTime> dateModified;

    public BaseModelDTO(){
        id = new SimpleIntegerProperty();
        dateCreated = new SimpleObjectProperty<DateTime>();
        dateModified = new SimpleObjectProperty<DateTime>();
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public DateTime getDateCreated() {
        return dateCreated.get();
    }

    public ObjectProperty<DateTime> dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public DateTime getDateModified() {
        return dateModified.get();
    }

    public ObjectProperty<DateTime> dateModifiedProperty() {
        return dateModified;
    }

    public void setDateModified(DateTime dateModified) {
        this.dateModified.set(dateModified);
    }
}
